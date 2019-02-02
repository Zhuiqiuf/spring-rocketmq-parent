package com.xiaochen.rocketmq.producer.service;


import com.xiaochen.rocketmq.producer.entity.UserInfo;

public interface PointService {
    int addPoint(UserInfo userInfo);
}
