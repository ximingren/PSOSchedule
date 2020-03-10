package com.ximingren.SpringTest;

import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.CourseScheduleSystemApplication;
import com.ximingren.CourseSchedule.Services.ICoursePlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ClassName SelectCoursePlanTest
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 20:23
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseScheduleSystemApplication.class)
public class SelectCoursePlanTest {
    @Autowired
    ICoursePlanService coursePlanService;
    @Test
    public void testBySemester() {
        QueryVO queryVO = new QueryVO();
        queryVO.setSemester("2015-2016-1");
        List<CoursePlan> coursePlanList = coursePlanService.selectAll(queryVO);
        for (CoursePlan coursePlan : coursePlanList) {
            System.out.println(coursePlan.toString());
        }
    }

    @Test
    public void testByCollege() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCollegeno("01");
        List<CoursePlan> coursePlanList = coursePlanService.selectAll(queryVO);
        for (CoursePlan coursePlan : coursePlanList) {
            System.out.println(coursePlan.toString());
        }
    }

    @Test
    public void testByVague() {
        QueryVO queryVO = new QueryVO();
        queryVO.setSemester("2015-2016-1");
        queryVO.setCondition("张");
        List<CoursePlan> coursePlanList = coursePlanService.selectAll(queryVO);
        System.out.println(coursePlanList.size());
        for (CoursePlan coursePlan : coursePlanList) {
            System.out.println(coursePlan.toString());
        }
    }

}
