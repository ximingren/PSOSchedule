package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassTaskDao {
    List<ClassTask> selectBySemester(ClassTask classTask);
}
