package com.auskat.demo.distributelock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 类文件: RedissonLockApplicationTests
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/10 0010
 * <p>
 * 时     间： 22:53
 * <p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedissonLockApplicationTests {

    @Autowired
    private RedissonClient redisson;

    @Test
    public void testRedissonLock() {
        // 1. Create config object
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://192.168.51.4:6379");
//
//        // 2. Create Redisson instance
//        // Sync and Async API
//        RedissonClient redisson = Redisson.create(config);
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
    }
}
