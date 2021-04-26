package com.auskat.demo.distributelock.controller;

import com.auskat.demo.distributelock.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 类文件: RedisLockController
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/9 0009
 * <p>
 * 时     间： 10:35
 * <p>
 */
@Slf4j
@RestController
public class RedisLockController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("redisLock")
    public String redisLock() {
        String threadName = Thread.currentThread().getName();
        log.info("{} 开始准备获取锁了", threadName);
        String key = "redisKey";
//        RedisLock redisLock = new RedisLock(redisTemplate, key,30);
//        boolean lock = redisLock.getLock();
//        if(lock) {
//            log.info("{} 已经成功获得了锁", threadName);
//            try {
//                // 模拟业务执行方法耗时15秒
//                Thread.sleep(15000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }finally {
//                boolean result = redisLock.unlock();
//                log.info(" 释放锁的结果，{}", result);
//            }
//        }
        // 自动释放锁的版本
        try (RedisLock redisLock = new RedisLock(redisTemplate, key, 30)) {
            if (redisLock.getLock()) {
                log.info("{} 已经成功获得了锁", threadName);
                // 模拟业务执行方法耗时15秒
                Thread.sleep(15000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("方法执行完成");
        return "方法执行完成";
    }
}
