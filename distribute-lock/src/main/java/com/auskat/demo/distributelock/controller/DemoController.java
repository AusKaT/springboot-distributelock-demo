package com.auskat.demo.distributelock.controller;

import com.auskat.demo.distributelock.dao.DistributeLockMapper;
import com.auskat.demo.distributelock.model.DistributeLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 类文件: DemoController
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/6 0006
 * <p>
 * 时     间： 16:55
 * <p>
 */
@Slf4j
@RestController
public class DemoController {

    @Resource
    private DistributeLockMapper distributeLockMapper;

    private Lock lock = new ReentrantLock();

    /**
     * 单体锁
     * @return 结果
     */
    @RequestMapping("singleLock")
    public String singleLock() {
        String threadName = Thread.currentThread().getName();
        log.info("{} 开始准备获取锁了", threadName);
        lock.lock();
        log.info("{} 已经成功获得了锁", threadName);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lock.unlock();

        return "我已经执行完成了!";
    }

    /**
     * 分布式锁
     * @return 结果
     * @throws Exception 错误信息
     */
    @RequestMapping("distributeLock")
    @Transactional(rollbackFor = Exception.class)
    public String distributeLock() throws Exception{
        String threadName = Thread.currentThread().getName();
        log.info("{} 开始准备获取锁了", threadName);
        DistributeLock distributeLock = distributeLockMapper.selectDistributeLock("demo");
        if(distributeLock == null) {
            throw new Exception("分布式锁找不到");
        }
        log.info("{} 已经成功获得了锁", threadName);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "我已经执行完成了!";
    }


}
