package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName CoursePlanDao
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 14:25
 */
@Mapper
public interface CoursePlanDao {
    int insertCoursePlan(CoursePlan coursePlan);

    int updateCoursePlan(ClassTask classTask);

    int deleteAllTable();

    //可以根据semeter,collegeNo,classNo,courseNo,teacherNo检索
    List<CoursePlan> selectAll(QueryVO queryVO);
}
