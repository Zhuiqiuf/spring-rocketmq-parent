package com.xiaochen.rocketmq.starter.config;

import com.xiaochen.rocketmq.starter.event.RocketmqEvent;
import com.xiaochen.rocketmq.starter.properties.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Zhuiqiuf
 * RocketMQ消息消费者 pull模式初始化自动装配
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RocketmqProperties.class)
@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "namesrvAddr")
public class DefaultMQPushConsumerAutoConfiguration {

    @Autowired
    private RocketmqProperties rocketmqProperties;

    @Value("${spring.application.name}")
    private String producerGroupName;

    @Value("${spring.application.name}")
    private String consumerGroupName;

    @Autowired
    private ApplicationEventPublisher publisher;
    
    /**
     * 初始化rocketmq主动拉取模式的消息监听方式的消费者
     */
    @Bean
    @ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "consumer.subscribe[0]")
    public DefaultMQPushConsumer pushConsumer() throws MQClientException{
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("push-"+consumerGroupName);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        consumer.setInstanceName(rocketmqProperties.getInstanceName());
        consumer.setConsumeMessageBatchMaxSize(rocketmqProperties.getConsumer().getConsumeMessageBatchMaxSize());//设置批量消费，以提升消费吞吐量，默认是1

        //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeThreadMin(rocketmqProperties.getConsumer().getConsumeThreadMin());
        consumer.setConsumeThreadMax(rocketmqProperties.getConsumer().getConsumeThreadMax());
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        if("BROADCASTING".equals(rocketmqProperties.getConsumer().getMessageModel())){
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }

        /**
         * 订阅指定topic下tags  topic:tag拼接
         */
        List<String> subscribeList = rocketmqProperties.getConsumer().getSubscribe();
        for (String sunscribe : subscribeList) {
            consumer.subscribe(sunscribe.split(":")[0], sunscribe.split(":")[1]);
        }

        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            MessageExt msg = msgs.get(0);
            try {
                //默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                log.info(Thread.currentThread().getName() + "Receive New Messages: " + msgs.size());
                //发布消息到达的事件，以便分发到每个tag的监听方法
                //throw new RuntimeException();
                this.publisher.publishEvent(new RocketmqEvent(msg,consumer,rocketmqProperties));
                log.info("消息到达事件已经发布成功！");
            } catch (Exception e) {
               log.error("msg reconsumeTimes_"+msg.getReconsumeTimes()+e);
                log.error("msg reconsumeTimes_"+msg.getMsgId()+e);
                if(msg.getReconsumeTimes()<3){//重复消费3次
                    //TODO 进行日志记录
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                } else {
                    //TODO 消息消费失败，进行日志记录
                }
            }
            //如果没有return success，consumer会重复消费此信息，直到success。
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        new Thread(() -> {
            try {
                Thread.sleep(5000);//延迟5秒再启动，主要是等待spring事件监听相关程序初始化完成，否则，会出现对RocketMQ的消息进行消费后立即发布消息到达的事件，然而此事件的监听程序还未初始化，从而造成消息的丢失
                /**
                 * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
                 */
                try {
                    consumer.start();
                } catch (Exception e) {
                    log.info("RocketMq pushConsumer Start failure!!!.");
                    e.printStackTrace();
                }
                log.info("RocketMq pushConsumer Started.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return consumer;
    }

}


