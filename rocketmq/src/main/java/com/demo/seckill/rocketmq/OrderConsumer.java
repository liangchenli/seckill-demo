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
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(topic = "seckill_order", consumerGroup = "seckill_order_group")
public class OrderConsumer implements RocketMQListener<MessageExt>
{
    private static final String TOPIC_NAME_ORDER_STATUS = "pay_check";

    @Autowired
    private RocketMqProducer rocketMQService;

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
            System.out.println("Received message:" + message);

            Order order = JSON.parseObject(message, Order.class);
            order.setCreateTime(new Date());
            boolean lockStockResult =
                    seckillActivityDao.lockStock(order.getSeckillActivityId());

            if (lockStockResult)
            {
                order.setOrderStatus(1);
            }
            else {
                order.setOrderStatus(0);
            }
            orderDao.insertOrder(order);
            log.info("Order created in DB: " + order.getOrderNo());
            rocketMQService.sendDelayMessage(TOPIC_NAME_ORDER_STATUS, JSON.toJSONString(order), 5);
            log.info("Countdown message sent " + order.getOrderNo());
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
