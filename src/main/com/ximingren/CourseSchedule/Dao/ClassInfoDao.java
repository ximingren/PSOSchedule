package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.ClassInfo;
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
public interface ClassInfoDao {
    int selectStudentNumber(@Param("classNo") String classNo);//获取班级的学生数量
    List<ClassInfo> selectAll();
}
