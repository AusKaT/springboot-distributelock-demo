package com.auskat.demo.distributelock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 类文件: Application
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/10 0010
 * <p>
 * 时     间： 22:54
 * <p>
 */
@SpringBootApplication
@ImportResource("classpath*:redisson.xml")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
