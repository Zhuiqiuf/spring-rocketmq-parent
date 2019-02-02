package com.xiaochen.rocketmq.starter.config;

import com.xiaochen.rocketmq.starter.properties.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Zhuiqiuf
 * RocketMQ普通消息生产者初始化自动装配
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RocketmqProperties.class)
@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "namesrvAddr")
public class DefaultMQProducerAutoConfiguration {

    @Autowired
    private RocketmqProperties rocketmqProperties;

    @Value("${spring.application.name}")
    private String producerGroupName;

    @Value("${spring.application.name}")
    private String consumerGroupName;

    /**
     * 初始化向rocketmq发送普通消息的生产者
     */
    @Bean
    @ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "producer.instanceName")
    public DefaultMQProducer defaultProducer() throws MQClientException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        DefaultMQProducer producer = new DefaultMQProducer(producerGroupName);
        producer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        producer.setInstanceName(rocketmqProperties.getProducer().getInstanceName());
        producer.setVipChannelEnabled(false);
        if(rocketmqProperties.getMaxMessageSize()!=null){
            producer.setMaxMessageSize(rocketmqProperties.getMaxMessageSize());
        }
        if(rocketmqProperties.getSendMsgTimeout()!=null){
            producer.setSendMsgTimeout(rocketmqProperties.getSendMsgTimeout());
        }
        //如果发送消息失败，设置重试次数，默认为2次
        if(rocketmqProperties.getRetryTimesWhenSendFailed()!=null){
            producer.setRetryTimesWhenSendFailed(rocketmqProperties.getRetryTimesWhenSendFailed());
        }

        /**
         * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         * 注意：切记不可以在每次发送消息时，都调用start方法
         */
        producer.start();
        log.info("RocketMq defaultProducer Started.");
        return producer;
    }
}


