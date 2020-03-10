package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.ClassroomLocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ClassroomLocationDao
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/7 17:50
 */
@Mapper
public interface ClassroomLocationDao {
    List<ClassroomLocation> selectByTeachBuildNo(@Param("teachBuildNo") String teachBuildNo);
}
