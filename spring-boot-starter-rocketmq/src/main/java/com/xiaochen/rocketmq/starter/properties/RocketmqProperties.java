package com.xiaochen.rocketmq.starter.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.xiaochen.rocketmq.starter.properties.RocketmqProperties.PREFIX;

/**
 * @author Zhuiqiuf
 * 这个类的属性对应application.properties文件中的配置项，
 * 目前只提供核心的一些配置支持，其他性能优化方面的配置参数可继续扩展
 */
@Component
@ConfigurationProperties(PREFIX)
public class RocketmqProperties {

    public static final String PREFIX = "spring.rocketmq";
    /**址列表，多个NameServer地址用分号隔开*/
    private String namesrvAddr;
    /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
    private String instanceName;
    /**客户端本机IP地址，某些机器会发送无法识别客户端IP地址的情况，需要应用在代码中强制指定*/
    private String clientIP;

    /**
     * 消息最大大小，默认4M
     */
    private Integer maxMessageSize ;
    /**
     * 消息发送超时时间，默认3秒
     */
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    private Integer retryTimesWhenSendFailed;

    /**生产者相关配置示例引用*/
    private RocketmqProperties.ProducerConfig producer;
    /**消费者相关配置示例引用*/
    private RocketmqProperties.ConsumerConfig consumer;

    public RocketmqProperties(){}

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public ProducerConfig getProducer() {
        return producer;
    }

    public void setProducer(ProducerConfig producer) {
        this.producer = producer;
    }

    public ConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerConfig consumer) {
        this.consumer = consumer;
    }

    public Integer getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(Integer maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public Integer getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(Integer sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public Integer getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(Integer retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    /**
     * @author Zhuiqiuf
     * RocketMQ消息生产者配置类,后续相关的配置还需继续扩展
     */
    public static class ProducerConfig {

        public ProducerConfig(){
            this.instanceName="1";
            this.checkThreadPoolMinSize=1;
        }

        /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
        private String instanceName;

        /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
        private String tranInstanceName;
        /**
         * Producer组名，多个Producer如果属于一个应用，发送同样的消息，则应该将他们归为同一组
         */
        private String producerGroup="DEFAULT_PRODUCER";
        /**
         * Broker回查Producer事务状态时，线程池大小
         */
        private Integer checkThreadPoolMinSize;
        /**
         * Broker回查Producer事务状态时，线程池大小
         */
        private Integer checkThreadPoolMaxSize = 1;
        /**
         * Broker回查Producer事务状态时，Produceer本地缓冲请求队列大小
         */
        private Integer checkRequestHoldMax = 2000;

        public Integer getCheckThreadPoolMinSize() {
            return checkThreadPoolMinSize;
        }

        public void setCheckThreadPoolMinSize(Integer checkThreadPoolMinSize) {
            this.checkThreadPoolMinSize = checkThreadPoolMinSize;
        }

        public Integer getCheckThreadPoolMaxSize() {
            return checkThreadPoolMaxSize;
        }

        public void setCheckThreadPoolMaxSize(Integer checkThreadPoolMaxSize) {
            this.checkThreadPoolMaxSize = checkThreadPoolMaxSize;
        }

        public Integer getCheckRequestHoldMax() {
            return checkRequestHoldMax;
        }

        public void setCheckRequestHoldMax(Integer checkRequestHoldMax) {
            this.checkRequestHoldMax = checkRequestHoldMax;
        }

        public String getProducerGroup() {
            return producerGroup;
        }

        public void setProducerGroup(String producerGroup) {
            this.producerGroup = producerGroup;
        }

        public String getInstanceName() {
            return instanceName;
        }

        public void setInstanceName(String instanceName) {
            this.instanceName = instanceName;
        }

        public String getTranInstanceName() {
            return tranInstanceName;
        }

        public void setTranInstanceName(String tranInstanceName) {
            this.tranInstanceName = tranInstanceName;
        }
    }

    /**
     * 消费者配置类
     */
    public static class ConsumerConfig {
        /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
        private String instanceName;

        /**客户端实例名称，客户端创建的多个Producer,Consumer实际是公用一个内部实例（这个实例包含网络连接，线程资源等）*/
        private String tranInstanceName;

        /**订阅的（topic:tag) list*/
        private List<String> subscribe;

        private String topic;

        private String tags;
        /**
         * 消息模型，支持以下两种：集群消费，广播消费
         */
        private String messageModel;
        /**消费线程池数量*/
        private Integer consumeThreadMin=20;
        /**消费线程池数量*/
        private Integer consumeThreadMax=64;
        /**批量消费，一次消费多少条消息*/
        private Integer consumeMessageBatchMaxSize=1;

        /**长轮询，Consumer拉消息请求在Broker挂起最长时间，单位毫秒*/
        private long brokerSuspendMaxTimeMillis=20000;
        /**非长轮询，拉消息超时时间，单位毫秒*/
        private long consumerPullTimeoutMillis=10000;
        /**长轮询，Consumer拉消息请求Broker挂起超过指定时间，客户端认为超时，单位毫秒*/
        private long consumerTimeoutMillisWhenSuspend=30000;
        /**注册的topic集合*/
        private Set<String> registerTopics;

        public  ConsumerConfig(){}

        public List<String> getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(List<String> subscribe) {
            this.subscribe = subscribe;
        }

        public Integer getConsumeThreadMin() {
            return consumeThreadMin;
        }

        public void setConsumeThreadMin(Integer consumeThreadMin) {
            this.consumeThreadMin = consumeThreadMin;
        }

        public Integer getConsumeThreadMax() {
            return consumeThreadMax;
        }

        public void setConsumeThreadMax(Integer consumeThreadMax) {
            this.consumeThreadMax = consumeThreadMax;
        }

        public Integer getConsumeMessageBatchMaxSize() {
            return consumeMessageBatchMaxSize;
        }

        public void setConsumeMessageBatchMaxSize(Integer consumeMessageBatchMaxSize) {
            this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
        }

        public String getMessageModel() {
            return messageModel;
        }

        public void setMessageModel(String messageModel) {
            this.messageModel = messageModel;
        }

        public long getBrokerSuspendMaxTimeMillis() {
            return brokerSuspendMaxTimeMillis;
        }

        public void setBrokerSuspendMaxTimeMillis(long brokerSuspendMaxTimeMillis) {
            this.brokerSuspendMaxTimeMillis = brokerSuspendMaxTimeMillis;
        }

        public long getConsumerPullTimeoutMillis() {
            return consumerPullTimeoutMillis;
        }

        public void setConsumerPullTimeoutMillis(long consumerPullTimeoutMillis) {
            this.consumerPullTimeoutMillis = consumerPullTimeoutMillis;
        }

        public long getConsumerTimeoutMillisWhenSuspend() {
            return consumerTimeoutMillisWhenSuspend;
        }

        public void setConsumerTimeoutMillisWhenSuspend(long consumerTimeoutMillisWhenSuspend) {
            this.consumerTimeoutMillisWhenSuspend = consumerTimeoutMillisWhenSuspend;
        }

        public Set<String> getRegisterTopics() {
            return registerTopics;
        }

        public void setRegisterTopics(Set<String> registerTopics) {
            this.registerTopics = registerTopics;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
    }

}


