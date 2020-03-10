package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.CollegeInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName CollegeInfoDao
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 16:23
 */
@Mapper
public interface CollegeInfoDao {
    List<CollegeInfo> selectAll();
}
