package com.demo.seckill.dataaccess.dao;

import com.demo.seckill.database.po.Order;

public interface OrderDao
{
    void insertOrder(Order order);

    Order queryOrder(String orderNo);

    void updateOrder(Order order);
}
