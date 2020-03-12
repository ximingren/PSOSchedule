package com.ximingren.SpringServiceTest;

import com.ximingren.CourseSchedule.Bean.po.*;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.CourseScheduleSystemApplication;
import com.ximingren.CourseSchedule.Services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ClassName ScheduleTest
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/8 11:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseScheduleSystemApplication.class)
public class ScheduleTest {
    @Autowired
    IClassScheduleService classScheduleService;
    @Autowired
    ITeachPlanService teachPlanService;

    @Autowired
    ITeachBuildInfoService teachBuildInfoService;

    @Test
    public void testSchedule() {
        classScheduleService.executeSchedule();
    }


    @Test
    public void selectTeachInfo() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCollegeno("01");
        List<TeachPlan> teachPlanList = teachPlanService.queryTeachPlan(queryVO);
        for (TeachPlan teachPlan : teachPlanList) {
            System.out.println(teachPlan.toString());
        }
    }


    @Test
    public void selectTeachBuildInfo() {
        QueryVO queryVO = new QueryVO();
        List<TeachBuildInfo> teachBuildInfoList = teachBuildInfoService.queryTeachBuildInfo(queryVO);
        for (TeachBuildInfo teachBuildInfo : teachBuildInfoList) {
            System.out.println(teachBuildInfo.toString());
        }
    }
}
