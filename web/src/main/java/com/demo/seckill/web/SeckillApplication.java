package com.demo.seckill.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.demo.seckill.database.mappers")
@ComponentScan(basePackages = {"com.demo.seckill"})
public class SeckillApplication {
	public static void main(String[] args)
	{
		SpringApplication.run(SeckillApplication.class, args);
	}
}
