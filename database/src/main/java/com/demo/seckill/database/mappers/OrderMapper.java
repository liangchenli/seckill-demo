package com.demo.seckill.database.mappers;

import com.demo.seckill.database.po.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    Order selectByOrderNo(String id);

    int updateByPrimaryKey(Order record);
}