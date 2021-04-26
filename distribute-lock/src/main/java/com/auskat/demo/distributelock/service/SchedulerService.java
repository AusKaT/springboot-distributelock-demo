package com.auskat.demo.distributelock.service;

import com.auskat.demo.distributelock.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 类文件: SchedulerService
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/9 0009
 * <p>
 * 时     间： 13:48
 * <p>
 */
@Slf4j
@Service
public class SchedulerService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendSms() {
        try (RedisLock redisLock = new RedisLock(redisTemplate, "autoSms", 30)) {
            if (redisLock.getLock()) {
                log.info("向 166******** 发送短信!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
