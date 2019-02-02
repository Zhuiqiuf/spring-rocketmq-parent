package com.xiaochen.rocketmq.consumer.service;


import com.xiaochen.rocketmq.consumer.entity.UserInfo;

public interface PointService {
    int addPoint(UserInfo userInfo);
}
