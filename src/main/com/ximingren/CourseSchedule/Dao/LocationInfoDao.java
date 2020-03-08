package com.ximingren.CourseSchedule.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName LocationInfoDao
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/7 17:47
 */
@Mapper
public interface LocationInfoDao {
    //根据学院获得教学楼编号
    String selectBuildNo(@Param("collegeNo") String collegeNo);

}
