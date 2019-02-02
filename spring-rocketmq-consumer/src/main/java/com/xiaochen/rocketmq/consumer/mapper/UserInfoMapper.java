package com.xiaochen.rocketmq.consumer.mapper;

import com.xiaochen.rocketmq.consumer.entity.PurchaseList;
import com.xiaochen.rocketmq.consumer.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Tangzy
 * 模拟 消费者加积分任务mapper：
 */
@Repository
public interface UserInfoMapper {

    /**
     * 根据主键查询规则
     * @param id
     * @return
     */
    PurchaseList selectByPrimaryKey(Long id);

    /**
     * 插入用户信息表数据
     * @param userInfo
     */
    int insert(UserInfo userInfo);

    /**
     *添加积分
     * @param userInfo
     */
    int addPoint(UserInfo userInfo);
}