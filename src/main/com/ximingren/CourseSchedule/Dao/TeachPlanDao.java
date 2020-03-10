package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.TeachPlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachPlanDao {
    List<TeachPlan> selectAll(QueryVO queryVO);
}
