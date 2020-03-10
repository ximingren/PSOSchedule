package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.TeachPlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;

import java.util.List;

public interface ITeachPlanService {
    List<TeachPlan> queryTeachPlan(QueryVO queryVO);
}
