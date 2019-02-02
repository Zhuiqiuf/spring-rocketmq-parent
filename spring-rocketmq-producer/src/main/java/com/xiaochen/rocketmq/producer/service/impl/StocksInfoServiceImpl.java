package com.xiaochen.rocketmq.producer.service.impl;


import com.alibaba.fastjson.JSON;
import com.xiaochen.rocketmq.producer.entity.StocksInfo;
import com.xiaochen.rocketmq.producer.mapper.StocksInfoMapper;
import com.xiaochen.rocketmq.producer.service.StocksInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * @author Zhuiqiuf 减库存
 */
@Service
@Slf4j
public class StocksInfoServiceImpl implements StocksInfoService {


    @Autowired
    private StocksInfoMapper stocksInfoMapper;

    @Transactional
    public int reduceStock(StocksInfo stocksInfo){
        log.info("receive msg and reduceStock start");
        int num=stocksInfoMapper.updateStocks(stocksInfo);
        return  num;
    }
}
