package com.auskat.demo.distributelock.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 类文件: RedissonController
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/10 0010
 * <p>
 * 时     间： 22:55
 * <p>
 */
@RestController
@Slf4j
public class RedissonLockController {

    @Autowired
    private RedissonClient redisson;

    /**
     * redisson分布式锁
     * API设置参数
     * @return 结果
     */
    @RequestMapping("redissonLockWithAPI")
    public String redissonLock() {
        log.info("进入了方法！");
        // 1. Create config object
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.51.4:6379").setPassword("auskat");

        // 2. Create Redisson instance
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        RLock rLock = redisson.getLock("order");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            log.info("获得了锁！");
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("释放了锁！");
            rLock.unlock();
        }
        log.info("方法执行完成！");
        return "方法执行完成";
    }

    /**
     * redisson分布式锁
     * springboot @Autowired 自动注入 application.properties
     * @return 结果
     */
    @RequestMapping("redissonLockWithSpringBoot")
    public String redissonLockWithSpringBoot() {
        log.info("进入了方法！");
        RLock rLock = redisson.getLock("order");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            log.info("获得了锁！");
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("释放了锁！");
            rLock.unlock();
        }
        log.info("方法执行完成！");
        return "方法执行完成";
    }

    /**
     * redisson分布式锁
     * spring方式 xml
     * @return 结果
     */
    @RequestMapping("redissonLockWithSpring")
    public String redissonLockWithSpring() {
        log.info("进入了方法！");
        RLock rLock = redisson.getLock("order");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            log.info("获得了锁！");
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("释放了锁！");
            rLock.unlock();
        }
        log.info("方法执行完成！");
        return "方法执行完成";
    }
}
