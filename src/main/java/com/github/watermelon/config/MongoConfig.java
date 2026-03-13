package com.github.watermelon.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * MongoDB 配置类
 * 替代原有的 Morphia 配置
 */
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    /**
     * 创建 MongoClient
     */
    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    /**
     * 创建 MongoDatabaseFactory
     */
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, getDatabaseName());
    }

    /**
     * 创建 MongoTemplate
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory, MongoConverter converter) {
        return new MongoTemplate(mongoDatabaseFactory, converter);
    }

    /**
     * 创建 MongoConverter
     * 移除 _class 字段，提高性能
     */
    @Bean
    public MongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory,
            MongoMappingContext context) {

        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);

        // 移除 _class 字段
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return converter;
    }

    /**
     * 从连接字符串中提取数据库名称
     */
    private String getDatabaseName() {
        try {
            ConnectionString connectionString = new ConnectionString(mongoUri);
            return connectionString.getDatabase();
        } catch (Exception e) {
            return "watermelon";
        }
    }
}