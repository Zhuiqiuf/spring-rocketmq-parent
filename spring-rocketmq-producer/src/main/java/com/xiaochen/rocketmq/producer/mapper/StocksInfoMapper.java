package com.xiaochen.rocketmq.producer.mapper;

import com.xiaochen.rocketmq.producer.entity.StocksInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Tangzy
 */
@Repository
public interface StocksInfoMapper {

    /**
     * 根据主键查询规则
     * @param id
     * @return
     */
    StocksInfo selectByPrimaryKey(Long id);

    /**
     * 插入库存数据
     * @param stocksInfo
     */
    int insert(StocksInfo stocksInfo);


    int updateStocks(StocksInfo stocksInfo);

}