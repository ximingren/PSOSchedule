package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;

import java.util.List;

public interface ICoursePlanService {
    List<CoursePlan> selectAll(QueryVO queryVO);

}
