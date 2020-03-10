package com.ximingren.CourseSchedule.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName ClassInfoDao
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/7 17:54
 */
@Mapper
public interface ClassInfoDao {
    int selectStudentNumber(@Param("classNo") String classNo);//获取班级的学生数量
}
