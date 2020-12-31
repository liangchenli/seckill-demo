package com.demo.seckill.requesthandling.Service;

import com.demo.seckill.requesthandling.Redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillActivityService
{
    private RedisService redisService;

    @Autowired
    public SeckillActivityService(RedisService redisService)
    {
        this.redisService = redisService;
    }

    /**
     * 判断商品是否还有库存
     * @param activityId 商品ID5. SeckillOverSellController 控制器编写 使用 lua 脚本
    处理抢购请求的方法
    6. 启动项目时 向 Redis 存入 商品库存
    创建 RedisPreheatRunner 类
     * @return
     */
    public boolean seckillStockValidator(long activityId)
    {
        String key = "stock:" + activityId;
        return redisService.stockDeductValidator(key);
    }

}
