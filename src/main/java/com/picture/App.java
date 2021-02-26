package com.picture;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan(basePackages = {"com.picture.business.mapper"})
@SpringBootApplication(scanBasePackages = {"com.springboot.simple","com.picture.business"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(com.picture.App.class);
    }
}
