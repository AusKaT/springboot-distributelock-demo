package com.auskat.demo.distributelock.zk;

import com.auskat.demo.distributelock.zk.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * 类文件: DistributeZkLockApplication
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/10 0010
 * <p>
 * 时     间： 11:17
 * <p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DistributeZkLockApplication {


    @Test
    public void testZkLock() throws Exception {
        ZkLock zkLock = new ZkLock();
        boolean lock = zkLock.getLock("order");
        log.info("获得锁的结果: {}", lock);
        zkLock.close();
    }

    @Test
    public void testCuratorLock() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                try {
                    // do some work inside of the critical section here
                    log.info("获得了锁");
                } finally {
                    lock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
