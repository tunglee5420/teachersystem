package com.just.teachersystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableConfigurationProperties
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.just.teachersystem.Mapper")
@EnableCaching
@Service("com.just.teachersystem.Service")
public class TeachersystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeachersystemApplication.class, args);
    }

}
