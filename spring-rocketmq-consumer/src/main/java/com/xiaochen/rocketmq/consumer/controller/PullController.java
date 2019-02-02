package com.xiaochen.rocketmq.consumer.controller;

import com.xiaochen.rocketmq.consumer.consumer.PullConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhuiqiuf
 * rocketMQ test ，用于作为消息生产者进行mq消息发送事务消息。
 */

@RestController
@Slf4j
public class PullController {

    @Autowired
    private PullConsumerService pullConsumerService;

    @RequestMapping(value = "/pullConsumer", method = RequestMethod.GET)
    public String pullConsumer() {
        pullConsumerService.pullConsumerFetchMsg();
        return "ok";
    }

}
