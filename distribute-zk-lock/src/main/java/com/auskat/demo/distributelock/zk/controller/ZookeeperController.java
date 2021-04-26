package com.auskat.demo.distributelock.zk.controller;

import com.auskat.demo.distributelock.zk.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 类文件: ZookeeperController
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/9 0009
 * <p>
 * 时     间： 16:06
 * <p>
 */
@RestController
@Slf4j
public class ZookeeperController {

    @Autowired
    private CuratorFramework client;

    @RequestMapping("zkLock")
    public String zkLock() {
        log.info("进入了方法");
        try(ZkLock zkLock = new ZkLock()) {
            if(zkLock.getLock("order")) {
                log.info("获得了锁");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("方法执行完成");
        return "方法执行完成";
    }

    @RequestMapping("curatorLock")
    public String curatorLock() {
        log.info("进入了方法");
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                log.info("获得了锁");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                log.info("释放了锁！！");
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("方法执行完成！");
        return "方法执行完成！";
    }

}
