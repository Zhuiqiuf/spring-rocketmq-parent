package com.xiaochen.rocketmq.producer.service;

import com.xiaochen.rocketmq.producer.entity.StocksInfo;

public interface StocksInfoService {
    int reduceStock(StocksInfo stocksInfo);
}
