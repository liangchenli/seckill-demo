package com.demo.seckill.web.Service;

import com.alibaba.fastjson.JSON;
import com.demo.seckill.dataaccess.dao.OrderDao;
import com.demo.seckill.dataaccess.dao.SeckillActivityDao;
import com.demo.seckill.database.po.Order;
import com.demo.seckill.database.po.SeckillActivity;
import com.demo.seckill.incrementalidgen.SnowFlakeIdGen;
import com.demo.seckill.rocketmq.RocketMqProducer;
import com.demo.seckill.web.Redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SeckillActivityService
{
    private static final String TOPIC_NAME_ORDER = "seckill_order";

    private RedisService redisService;
    private RocketMqProducer rocketMQService;
    private SeckillActivityDao seckillActivityDao;

    private SnowFlakeIdGen snowFlake = new SnowFlakeIdGen(1, 1);

    @Autowired
    OrderDao orderDao;

    @Autowired
    public SeckillActivityService(RedisService redisService, RocketMqProducer rocketMQService, SeckillActivityDao seckillActivityDao)
    {
        this.redisService = redisService;
        this.rocketMQService = rocketMQService;
        this.seckillActivityDao = seckillActivityDao;
    }

    /**
     * 判断商品是否还有库存
     * @param activityId 商品ID5. SeckillOverSellController 控制器编写 使用 lua 脚本
    处理抢购请求的方法
    6. 启动项目时 向 Redis 存入 商品库存
    创建 RedisPreheatRunner 类
     * @return
     */
    public boolean seckillStockValidateAndDeduct(long activityId)
    {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }

    public Order createOrder(long seckillActivityId, long userId) throws Exception
    {
        SeckillActivity seckillActivity =
                seckillActivityDao.querySeckillActivityById(seckillActivityId);
        Order order = new Order();

        order.setOrderNo(String.valueOf(snowFlake.nextId()));
        order.setSeckillActivityId(seckillActivity.getId());
        order.setUserId(userId);
        order.setOrderAmount(seckillActivity.getSeckillPrice().longValue());
        /*
         *2.发送创建订单消息
         */
        rocketMQService.sendMessage(TOPIC_NAME_ORDER, JSON.toJSONString(order));

        return order;
    }

    public void payOrderProcess(String orderNo) {
        System.out.println("完成支付订单 订单号：" + orderNo);
        Order order = orderDao.queryOrder(orderNo);
        boolean deductStockResult =
                seckillActivityDao.deductStock(order.getSeckillActivityId());
        if (deductStockResult) {
            order.setPayTime(new Date());
// 订单状态 0、没有可用库存，无效订单 1、已创建等待支付 2、完成支付
            order.setOrderStatus(2);
            orderDao.updateOrder(order);
        }
    }
}
