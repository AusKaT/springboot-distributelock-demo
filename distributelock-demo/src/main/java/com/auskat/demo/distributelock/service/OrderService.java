package com.auskat.demo.distributelock.service;

import com.auskat.demo.distributelock.dao.OrderMapper;
import com.auskat.demo.distributelock.dao.ProductMapper;
import com.auskat.demo.distributelock.model.Order;
import com.auskat.demo.distributelock.model.OrderItem;
import com.auskat.demo.distributelock.model.Product;
import com.auskat.demo.distributelock.dao.OrderItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ProductMapper productMapper;

    /**
     * 平台数据管理器
     */
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 事务的定义
     */
    @Autowired
    private TransactionDefinition transactionDefinition;

    //购买商品id
    private int purchaseProductId = 100100;

    //购买商品数量
    private int purchaseProductNum = 1;

    private Object object = new Object();

    /**
     * 可重入锁
     */
    private Lock lock = new ReentrantLock();


    /**
     * 插入订单数
     *
     * @param product 商品信息
     * @return 订单ID
     */
    public Integer insertOrder(Product product) {
        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        order.setOrderStatus(1);//待处理
        order.setReceiverName("xxx");
        order.setReceiverMobile("13311112222");
        order.setCreateTime(new Date());
        order.setCreateUser("xxx");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xxx");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItem.setCreateUser("xxx");
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xxx");
        orderItemMapper.insertSelective(orderItem);

        return order.getId();
    }

    /**
     * 方法锁解决超卖问题
     *
     * @return 结果
     * @throws Exception 异常
     */
//    @Transactional(rollbackFor = Exception.class)
    public synchronized Integer createOrderWithMethodSynchronized() throws Exception {
        // 自定义事务
        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        Product product = productMapper.selectByPrimaryKey(purchaseProductId);

        if (product == null) {
            platformTransactionManager.rollback(transaction);
            throw new Exception("购买商品：" + purchaseProductId + "不存在");
        }

        //商品当前库存
        Integer currentCount = product.getCount();

        System.out.println(Thread.currentThread().getName() + ":库存数 " + currentCount);

        //校验库存
        if (purchaseProductNum > currentCount) {
            platformTransactionManager.rollback(transaction);
            throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }


//        // 计算剩余库存
//        Integer leftCount = currentCount - purchaseProductNum;
//        // 更新库存
//        product.setCount(leftCount);
//        product.setUpdateTime(new Date());
//        product.setUpdateUser("xxxx");
//        productMapper.updateByPrimaryKeySelective(product);


        // 数据库 -- update 更新锁方式实现

        productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());

        // 检索商品的库存
        // 如果商品为负数，则抛出异常


        Integer orderId = this.insertOrder(product);

        // 事务提交
        platformTransactionManager.commit(transaction);
        return orderId;
    }


    /**
     * 块锁解决超卖问题
     *
     * @return 结果
     * @throws Exception 异常
     */
//    @Transactional(rollbackFor = Exception.class)
    public synchronized Integer createOrderWithBlockSynchronized() throws Exception {
        // 自定义事务
        Product product = new Product();

        // 1 this orderService实例锁
        // 2 object object 对象锁（单例，object只有一个 等同于 this)
        // 3 orderService.class 获取orderService类锁（如果不是单例的对象，直接锁类，范围大于this）
        synchronized (this) {
            TransactionStatus transaction2 = platformTransactionManager.getTransaction(transactionDefinition);
            product = productMapper.selectByPrimaryKey(purchaseProductId);

            if (product == null) {
                platformTransactionManager.rollback(transaction2);
                throw new Exception("购买商品：" + purchaseProductId + "不存在");
            }

            //商品当前库存
            Integer currentCount = product.getCount();

            System.out.println(Thread.currentThread().getName() + ":库存数 " + currentCount);

            //校验库存
            if (purchaseProductNum > currentCount) {
                platformTransactionManager.rollback(transaction2);
                throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }

            // 数据库 -- update 更新锁方式实现

            productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());
            platformTransactionManager.commit(transaction2);
        }


        // 检索商品的库存
        // 如果商品为负数，则抛出异常

        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        Integer orderId = this.insertOrder(product);

        // 事务提交
        platformTransactionManager.commit(transaction);
        return orderId;
    }


    /**
     * 可重入锁 解决超卖问题
     *
     * @return 结果
     * @throws Exception 异常
     */
//    @Transactional(rollbackFor = Exception.class)
    public synchronized Integer createOrderWithReentrantLock() throws Exception {
        // 自定义事务
        Product product = new Product();
        // 加锁
        lock.lock();
        try {
            TransactionStatus transaction2 = platformTransactionManager.getTransaction(transactionDefinition);
            product = productMapper.selectByPrimaryKey(purchaseProductId);

            if (product == null) {
                platformTransactionManager.rollback(transaction2);
                throw new Exception("购买商品：" + purchaseProductId + "不存在");
            }

            //商品当前库存
            Integer currentCount = product.getCount();

            System.out.println(Thread.currentThread().getName() + ":库存数 " + currentCount);

            //校验库存
            if (purchaseProductNum > currentCount) {
                platformTransactionManager.rollback(transaction2);
                throw new Exception("商品" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }

            // 数据库 -- update 更新锁方式实现

            productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());
            platformTransactionManager.commit(transaction2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }


        // 检索商品的库存
        // 如果商品为负数，则抛出异常

        TransactionStatus transaction = platformTransactionManager.getTransaction(transactionDefinition);

        assert product != null;
        Integer orderId = this.insertOrder(product);

        // 事务提交
        platformTransactionManager.commit(transaction);
        return orderId;
    }


}
