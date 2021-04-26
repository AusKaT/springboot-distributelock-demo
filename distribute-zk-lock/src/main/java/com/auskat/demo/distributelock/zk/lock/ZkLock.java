package com.auskat.demo.distributelock.zk.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 类文件: ZkLock
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
@Slf4j
public class ZkLock implements AutoCloseable, Watcher {

    private ZooKeeper zooKeeper;

    private String zNode;

    /**
     * 初始化
     *
     * @throws IOException 异常
     */
    public ZkLock() throws IOException {
        Watcher watcher;
        this.zooKeeper = new ZooKeeper("localhost:2181", 10000, this);
    }

    /**
     * 获得锁
     *
     * @param businessCode 业务Code
     * @return 结果
     */
    public boolean getLock(String businessCode) {
        try {
            // 创建业务根节点
            Stat stat = zooKeeper.exists("/" + businessCode, false);
            if (stat == null) {
                zooKeeper.create("/" + businessCode, businessCode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            // 创建瞬时有序节点 /order/order_00000001
            zNode = zooKeeper.create("/" + businessCode + "/" + businessCode + "_", businessCode.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            // 获取业务节点下所有的子节点
            List<String> childrenNodes = zooKeeper.getChildren("/" + businessCode, false);
            // 子节点排序
            Collections.sort(childrenNodes);
            // 获取序号最小的（第一个）子节点
            String firstNode = childrenNodes.get(0);
            // 如果创建的节点是第一个子节点，则获得锁
            if (zNode.endsWith(firstNode)) {
                return true;
            }
            // 不是第一个子节点，则监听前一个节点
            String lastNode = firstNode;
            for (String node : childrenNodes) {
                if (zNode.endsWith(node)) {
                    zooKeeper.exists("/" + businessCode + "/" + lastNode, true);
                    break;
                } else {
                    lastNode = node;
                }
            }
            synchronized (this) {
                wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * 释放锁
     * @throws Exception 异常
     */
    @Override
    public void close() throws Exception {
        zooKeeper.delete(zNode, -1);
        zooKeeper.close();
        log.info("我已经释放了锁");
    }

    /**
     * 监听回调
     * @param event 事件
     */
    @Override
    public void process(WatchedEvent event) {
        if(event.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify();
            }
        }
    }
}
