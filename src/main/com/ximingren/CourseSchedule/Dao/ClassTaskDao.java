package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassTaskDao {
    //获得某学期的排课计划
    List<ClassTask> selectBySemester(ClassTask classTask);
//  通过列名获取该列的唯一值列表
    List<String> selectByColumnName(@Param("columnName") String columnName);

    List<ClassTask> selectAll(QueryVO queryVO);
}
