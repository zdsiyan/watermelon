package com.github.watermelon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB审计配置
 * 启用 @CreatedDate, @LastModifiedDate 等审计注解
 */
@Configuration
@EnableMongoAuditing
public class MongoAuditingConfig {
    // 配置类本身为空，注解已经启用了审计功能
}