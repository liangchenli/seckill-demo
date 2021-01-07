package com.demo.seckill.rocketmq;

import com.alibaba.fastjson.JSON;
import com.demo.seckill.dataaccess.dao.OrderDao;
import com.demo.seckill.dataaccess.dao.SeckillActivityDao;
import com.demo.seckill.database.po.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RocketMQMessageListener(topic = "pay_check", consumerGroup = "seckill_order_check_group")
public class OrderStatusCheckConsumer implements RocketMQListener<MessageExt>
{
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Override
    @Transactional
    public void onMessage(MessageExt messageExt)
    {
        try
        {
            String message = new String(messageExt.getBody(), "UTF-8");
            log.info("Received message:" + message);

            Order order = JSON.parseObject(message, Order.class);
            Order orderInfo = orderDao.queryOrder(order.getOrderNo());

            if (orderInfo.getOrderStatus() != 2) {
                log.info("Order overdue, order number is " + orderInfo.getOrderNo());
                orderInfo.setOrderStatus(99);
                orderDao.updateOrder(orderInfo);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
}
