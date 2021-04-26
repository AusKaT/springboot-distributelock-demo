package com.auskat.demo.distributelock.dao;

import com.auskat.demo.distributelock.model.DistributeLock;
import com.auskat.demo.distributelock.model.DistributeLockExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类文件: DistributeLockMapper.xml
 * <p>
 * <p>
 * 类描述：
 * <p>
 * 作     者： AusKa_T
 * <p>
 * 日     期： 2021/4/8 0008
 * <p>
 * 时     间： 20:05
 * <p>
 */
public interface DistributeLockMapper {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    long countByExample(DistributeLockExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int deleteByExample(DistributeLockExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int insert(DistributeLock record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int insertSelective(DistributeLock record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    List<DistributeLock> selectByExample(DistributeLockExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    DistributeLock selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int updateByExampleSelective(@Param("record") DistributeLock record, @Param("example") DistributeLockExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int updateByExample(@Param("record") DistributeLock record, @Param("example") DistributeLockExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int updateByPrimaryKeySelective(DistributeLock record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table distribute_lock
     *
     * @mbg.generated Tue Aug 13 17:54:34 CST 2019
     */
    int updateByPrimaryKey(DistributeLock record);


    DistributeLock selectDistributeLock(@Param("businessCode") String businessCode);
}