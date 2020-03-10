package com.ximingren.SpringTest;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.po.CollegeInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.CourseScheduleSystemApplication;
import com.ximingren.CourseSchedule.Services.IClassTaskService;
import com.ximingren.CourseSchedule.Services.ICollegeInfoService;
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
public class SelectClassTask {
    @Autowired
    IClassTaskService classTaskService;
    @Test
    public void testCollegeInfo() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCollegeno("01");
        List<ClassTask> classTaskList = classTaskService.queryClassTask(queryVO);
        for (ClassTask classTask : classTaskList) {
            System.out.println(classTask.toString());
        }
    }

    @Test
    public void testVague() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCollegeno("01");
        queryVO.setCondition("1班");
        List<ClassTask> classTaskList = classTaskService.queryClassTask(queryVO);
        for (ClassTask classTask : classTaskList) {
            System.out.println(classTask.toString());
        }

    }

}
