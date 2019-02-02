package com.xiaochen.rocketmq.producer.service.impl;

import com.alibaba.fastjson.JSON;
import com.xiaochen.rocketmq.producer.entity.UserInfo;
import com.xiaochen.rocketmq.producer.mapper.UserInfoMapper;
import com.xiaochen.rocketmq.producer.service.PointService;
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
 * @author Zhuiqiuf 加积分操作
 */
@Service
@Slf4j
public class PointServiceImpl implements PointService {



    @Autowired
    private UserInfoMapper userInfoMapper;

    @Transactional
    public int addPoint(UserInfo userInfo){
        log.info("receive msg and addPoint start");
        int num= userInfoMapper.addPoint(userInfo);

        return num;
    }
}
