package com.xiaochen.rocketmq.producer.service;


import com.xiaochen.rocketmq.producer.entity.PurchaseList;

public interface PurchaseService {
    int addPurchaseList(PurchaseList purchaseList, String orderNumber);
}
