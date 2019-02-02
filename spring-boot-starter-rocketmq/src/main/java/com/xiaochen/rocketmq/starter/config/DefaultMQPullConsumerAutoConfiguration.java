package com.xiaochen.rocketmq.starter.config;

import com.xiaochen.rocketmq.starter.properties.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author Zhuiqiuf
 * RocketMQ消息消费者 pull模式初始化自动装配
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RocketmqProperties.class)
@ConditionalOnProperty(prefix = RocketmqProperties.PREFIX, value = "namesrvAddr")
public class DefaultMQPullConsumerAutoConfiguration {

    @Autowired
    private RocketmqProperties rocketmqProperties;

    @Value("${spring.application.name}")
    private String producerGroupName;

    @Value("${spring.application.name}")
    private String consumerGroupName;

    /**
     * 初始化rocketmq 需要时拉取的消费模式的消费者
     */
    @Bean
    @Qualifier("pullConsumer")
    public DefaultMQPullConsumer pullConsumer(){
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPullConsumer consumer=new DefaultMQPullConsumer("pull-"+consumerGroupName);
        consumer.setNamesrvAddr(rocketmqProperties.getNamesrvAddr());
        consumer.setInstanceName(rocketmqProperties.getInstanceName());
        consumer.setBrokerSuspendMaxTimeMillis(rocketmqProperties.getConsumer().getBrokerSuspendMaxTimeMillis());
        consumer.setConsumerPullTimeoutMillis(rocketmqProperties.getConsumer().getConsumerPullTimeoutMillis());
        consumer.setConsumerTimeoutMillisWhenSuspend(rocketmqProperties.getConsumer().getConsumerTimeoutMillisWhenSuspend());
        /**
         * 设置消费模型，集群还是广播，默认为集群
         */
        if("BROADCASTING".equals(rocketmqProperties.getConsumer().getMessageModel())){
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }
        //注册的topic集合
        consumer.setRegisterTopics(rocketmqProperties.getConsumer().getRegisterTopics());
        //consumer.setMessageQueueListener(new);
        return consumer;
    }
}


