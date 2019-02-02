package com.xiaochen.rocketmq.producer.controller;

import com.xiaochen.rocketmq.producer.listener.TransactionListenerImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private DefaultMQProducer defaultProducer;

    @Autowired
    private TransactionMQProducer transactionProducer;
    @Autowired
    private TransactionListenerImpl transactionListenerImpl;

    @Value("${spring.rocketmq.producer.topic}")
    private String producerTopic;


    @RequestMapping(value = "/sendMsg", method = RequestMethod.GET)
    public void sendMsg() {
        Message msg = new Message(producerTopic,// topic
                "TagC",// tag
                "OrderID001",// key
                ("Hello zhuiqiuf").getBytes());// body
        msg.setDelayTimeLevel(3);
        //defaultProducer.send(messages);
        try {
            defaultProducer.send(msg,new SendCallback(){//消息发送异步方式
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                    //TODO 发送成功处理
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(e);
                    //TODO 发送失败处理
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/sendTransactionMsg", method = RequestMethod.GET)
    public String sendTransactionMsg(HttpServletRequest req) {
        transactionProducer.setTransactionListener(transactionListenerImpl);
        SendResult sendResult = null;
        try {
            //构造消息
            Message msg = new Message(producerTopic,// topic
                    "TagA",// tag
                    "OrderID001",// key
                    (req.getParameter("msg")).getBytes());// body

            sendResult = transactionProducer.sendMessageInTransaction(msg, null);
           log.info("msg send:",sendResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendResult.toString();
    }
}

