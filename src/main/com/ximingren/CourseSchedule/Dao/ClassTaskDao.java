package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClassTaskDao {
    //获得某学期的开课计划
    List<ClassTask> selectBySemester(ClassTask classTask);

    //  通过列名获取该列的唯一值列表
    List<String> selectByColumnName(@Param("columnName") String columnName);

    //查询开课计划
    List<ClassTask> selectAll(QueryVO queryVO);

    //保存开课计划
    int insert(ClassTask classTask);

    //更新开课计划
    int updateByPrimaryKey(ClassTask classTask);
    //删除开课计划
    int deleteByPrimaryKey(ClassTask classTask);

    //获得总数量数
    int getCount();
}
