package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Dao.CoursePlanDao;
import com.ximingren.CourseSchedule.Services.ICoursePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName CoursePlanService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 15:34
 */
@Service
public class CoursePlanService implements ICoursePlanService {
    @Autowired
    CoursePlanDao coursePlanDao;

    @Override
    public List<CoursePlan> selectAll(QueryVO queryVO) {
        try {
            List<CoursePlan> coursePlanList = coursePlanDao.selectAll(queryVO);
            return coursePlanList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
