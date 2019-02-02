package com.xiaochen.rocketmq.consumer.consumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RocketMq pull消费类型测试代码
 * 需设置消费类型为是否为pull，不设置为pull则默认为push
 * spring.rocketmq.consumer.pullConsumeEnable=true
 */
@Component
@Slf4j
public class PullConsumerService {


    @Autowired
    @Qualifier("pullConsumer")
    private DefaultMQPullConsumer pullConsumer;

    @Autowired
    @Qualifier("noBalanceRestTemplate")
    private RestTemplate noBalanceRestTemplate;

    //保存上一次消费的消息位置
    private static final Map offsetTable = new HashMap();

    public void pullConsumerFetchMsg(){
        Set<MessageQueue> mqs = null;
        try {
            pullConsumer.start();
            //拉取PullConsumerTopicTest topic下的所有消息队列
            mqs = pullConsumer.fetchSubscribeMessageQueues("testTopic");
        } catch (MQClientException e) {
            log.error("connet MQClient exception!!"+e);
            return;
        }
        //遍历消息队列
        for (MessageQueue mq : mqs) {
            System.err.println("Consume from the queue: " + mq);
            SINGLE_MQ:
            while (true) {
                try {
                    //设置上次消费消息下标
                    PullResult pullResult = pullConsumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    System.out.println(pullResult);
                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                    switch (pullResult.getPullStatus()) {
                        //根据结果状态，如果找到消息，批量消费消息
                        case FOUND:
                            List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                            for (MessageExt m : messageExtList) {
                                String url="http://192.168.212.73:8901/oauth/checkToken";//鉴权
                                log.info("Remote invocation of third-party services");
                                String body=new String(m.getBody(),"utf-8");
                                HttpEntity<String> httpEntity = new HttpEntity<>(body);
                                ResponseEntity<String> entity = noBalanceRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                                log.info("receive msg"+entity.toString());
                            }
                            break;
                        case NO_MATCHED_MSG:
                            break;
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        pullConsumer.shutdown();
    }
    //保存上次消费的消息下标，这里使用了一个全局HashMap来保存
    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        offsetTable.put(mq, offset);
    }

    //获取上次消费的消息的下标
    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = (Long) offsetTable.get(mq);
        if (offset != null) {
            return offset;
        }
        return 0;
    }
}

