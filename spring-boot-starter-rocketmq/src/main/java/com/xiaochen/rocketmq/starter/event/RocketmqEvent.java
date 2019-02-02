package com.xiaochen.rocketmq.starter.event;

import com.xiaochen.rocketmq.starter.properties.RocketmqProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationEvent;

import java.io.UnsupportedEncodingException;

/**
 * @author tangzy
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RocketmqEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4468405250074063206L;

    private DefaultMQPushConsumer consumer;
    private MessageExt messageExt;
    private String topic;
    private String tag;
    private byte[] body;
    private RocketmqProperties rocketmqProperties;

    public RocketmqEvent(MessageExt msg,DefaultMQPushConsumer consumer,RocketmqProperties rocketmqProperties) {
        super(msg);
        this.topic = msg.getTopic();
        this.tag = msg.getTags();
        this.body = msg.getBody();
        this.consumer = consumer;
        this.messageExt = msg;
        this.rocketmqProperties=rocketmqProperties;
    }

    public String getMsg() {
        try {
            return new String(this.body,"utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getMsg(String code) {
        try {
            return new String(this.body,code);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}
