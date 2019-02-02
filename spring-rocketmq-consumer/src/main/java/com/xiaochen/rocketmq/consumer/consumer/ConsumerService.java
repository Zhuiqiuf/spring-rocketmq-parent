package com.xiaochen.rocketmq.consumer.consumer;


import com.alibaba.fastjson.JSON;
import com.xiaochen.rocketmq.consumer.entity.StocksInfo;
import com.xiaochen.rocketmq.consumer.entity.UserInfo;
import com.xiaochen.rocketmq.consumer.service.PointService;
import com.xiaochen.rocketmq.consumer.service.StocksInfoService;
import com.xiaochen.rocketmq.starter.event.RocketmqEvent;
import com.xiaochen.rocketmq.starter.properties.RocketmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Optional;

/**
 * 消息消费者，监听rocketmq消费者接受到消息后发布的事件，然后进行业务逻辑处理。
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(RocketmqProperties.class)
public class ConsumerService {
    @Autowired
    private PointService pointService;

    @Autowired
    public RocketmqProperties rocketmqProperties;

    @Autowired
    private StocksInfoService stocksInfoService;


    @EventListener(condition = "#event.topic==@consumerService.rocketmqProperties.consumer.topic && #event.tag==@consumerService.rocketmqProperties.consumer.tags")
    public void addPoint(RocketmqEvent event){
        DefaultMQPushConsumer consumer = event.getConsumer();
        log.info("receive msg and addPoint start");
        try {
            String params=event.getMsg("utf-8");
            Optional<UserInfo> userInfo = Optional.of(JSON.parseObject(params, UserInfo.class));
            userInfo.ifPresent(pointService::addPoint);//userInfo 存在则利用方法引用调用pointService的addPoint方法
            //pointService.addPoint();
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

    /**
     *
     * @param event
     */

    @EventListener(condition = "#event.topic=='testTopic' && #event.tag=='TagB'")
    public void reduceStock(RocketmqEvent event){
        DefaultMQPushConsumer consumer = event.getConsumer();
        try {
            log.info("receive msg and reduceStock start");
            String params=event.getMsg("utf-8");
            Optional<StocksInfo> stocksInfo = Optional.of(JSON.parseObject(params, StocksInfo.class));
            stocksInfo.ifPresent(stocksInfoService::reduceStock);//stocksInfo 存在则利用方法引用调用stocksInfoService的reduceStock方法
            //stocksInfoService.reduceStock(stocksInfo);
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

