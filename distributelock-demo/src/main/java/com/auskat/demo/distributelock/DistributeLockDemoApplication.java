package com.auskat.demo.distributelock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.auskat.demo.distributelock.dao")
public class DistributeLockDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeLockDemoApplication.class, args);
    }

}
