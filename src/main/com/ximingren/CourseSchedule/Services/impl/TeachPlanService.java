package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.TeachPlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Dao.TeachPlanDao;
import com.ximingren.CourseSchedule.Services.ITeachPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TeachPlanService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 16:55
 */
@Service
public class TeachPlanService implements ITeachPlanService {
    @Autowired
    TeachPlanDao teachPlanDao;
    @Override
    public List<TeachPlan> queryTeachPlan(QueryVO queryVO) {
        List<TeachPlan> teachPlanVOList = teachPlanDao.selectAll(queryVO);
        return teachPlanVOList;
    }
}
