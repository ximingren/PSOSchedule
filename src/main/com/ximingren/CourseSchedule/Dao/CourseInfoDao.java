package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.ClassInfo;
import com.ximingren.CourseSchedule.Bean.po.CourseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ClassInfoDao
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/7 17:54
 */
@Mapper
public interface CourseInfoDao {
    List<CourseInfo> selectAll();
}
