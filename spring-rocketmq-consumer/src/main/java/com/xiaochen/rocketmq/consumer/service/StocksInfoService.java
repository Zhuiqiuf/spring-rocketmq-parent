package com.xiaochen.rocketmq.consumer.service;

import com.xiaochen.rocketmq.consumer.entity.StocksInfo;

public interface StocksInfoService {
    int reduceStock(StocksInfo stocksInfo);
}
