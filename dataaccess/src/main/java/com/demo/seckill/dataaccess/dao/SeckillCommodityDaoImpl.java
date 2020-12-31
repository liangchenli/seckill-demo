package com.demo.seckill.dataaccess.dao;

import com.demo.seckill.database.po.SeckillCommodity;
import com.demo.seckill.database.mappers.SeckillCommodityMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class SeckillCommodityDaoImpl implements SeckillCommodityDao {

    @Resource
    private SeckillCommodityMapper seckillCommodityMapper;

    @Override
    public SeckillCommodity querySeckillCommodityById(long commodityId) {
        return seckillCommodityMapper.selectByPrimaryKey(commodityId);
    }
}
