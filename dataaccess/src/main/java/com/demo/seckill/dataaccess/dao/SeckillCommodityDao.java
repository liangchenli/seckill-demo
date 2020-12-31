package com.demo.seckill.dataaccess.dao;

import com.demo.seckill.database.po.SeckillCommodity;

public interface SeckillCommodityDao {

    public SeckillCommodity querySeckillCommodityById(long commodityId);
}
