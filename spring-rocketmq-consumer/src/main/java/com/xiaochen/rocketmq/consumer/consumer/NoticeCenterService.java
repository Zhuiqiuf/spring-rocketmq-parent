package com.xiaochen.rocketmq.consumer.consumer;


import com.xiaochen.rocketmq.starter.event.RocketmqEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 消息消费者，监听rocketmq消费者接受到消息后发布的事件，然后进行远程应用调用。
 */
@Component
@Slf4j
public class NoticeCenterService {

    @Autowired
    @Qualifier("noBalanceRestTemplate")
    protected RestTemplate noBalanceRestTemplate;


    /**
     * 消息事件通知第三方服务
     * @param event
     */

    //@EventListener(condition = "#event.topic=='testTopic' && #event.tag=='TagC'")
    public void EventNotice(RocketmqEvent event){
        DefaultMQPushConsumer consumer = event.getConsumer();
        String url="http://192.168.212.73:8901/oauth/checkToken";//鉴权
        try {
            log.info("Remote invocation of third-party services");
            String body=event.getMsg("utf-8");
           // UserInfo userInfo=JSON.parseObject(body, UserInfo.class);
            HttpEntity<String> httpEntity = new HttpEntity<>(body);
            ResponseEntity<String> entity = noBalanceRestTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            log.info("receive msg"+entity.toString());
        } catch (Exception e) {
            log.error("message consumer error should back!");
            if (event.getMessageExt().getReconsumeTimes() <= 3) {//重复消费3次
                try {
                    consumer.sendMessageBack(event.getMessageExt(), 2);
                } catch (Exception e1) {
                    //TODO 消息消费失败，进行日志记录
                }
            } else {
                //TODO 消息消费失败，进行日志记录
            }
        }
    }
}

