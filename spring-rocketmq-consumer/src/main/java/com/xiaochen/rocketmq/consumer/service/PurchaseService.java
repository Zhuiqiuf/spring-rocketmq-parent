package com.xiaochen.rocketmq.consumer.service;


import com.xiaochen.rocketmq.consumer.entity.PurchaseList;

public interface PurchaseService {
    int addPurchaseList(PurchaseList purchaseList, String orderNumber);
}
