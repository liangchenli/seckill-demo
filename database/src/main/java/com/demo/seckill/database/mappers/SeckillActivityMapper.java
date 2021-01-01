package com.demo.seckill.database.mappers;

import com.demo.seckill.database.po.SeckillActivity;

import java.util.List;

public interface SeckillActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivity record);

    int insertSelective(SeckillActivity record);

    SeckillActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivity record);

    int updateByPrimaryKey(SeckillActivity record);

    int lockStock(Long id);

    List<SeckillActivity> querySeckillActivitysByStatus(int activityStatus);
}