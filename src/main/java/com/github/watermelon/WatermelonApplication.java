package com.github.watermelon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Watermelon Spring Boot 主启动类
 *
 * 排除MongoDB自动配置，使用自定义配置
 */
@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
public class WatermelonApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatermelonApplication.class, args);
    }
}