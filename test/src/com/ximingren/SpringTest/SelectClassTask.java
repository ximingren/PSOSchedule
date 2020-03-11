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

    @Test
    public void testInsert() {
        ClassTask classTask = new ClassTask();
        classTask.setId((long) 117);
        classTask.setSemester("2016-2017-1");
        classTask.setCollegeno("02");
        classTask.setClassno("2016020101");
        classTask.setCourseno("100014");
        classTask.setTeacherno("02100001");
        classTask.setCourseattr("01");
        classTask.setStudentnumber(30);
        classTask.setWeekssum(9);
        classTask.setWeeksnumber(4);
        classTask.setIsfix("2");
        classTask.setClasstime("01");
        boolean flag = classTaskService.saveClassTask(classTask);
        System.out.println("保存成功？："+flag);
    }

    @Test
    public void testDelete() {
        ClassTask classTask = new ClassTask();
        classTask.setId((long) 117);
        boolean flag = classTaskService.deleteClassTask(classTask);
        System.out.println("删除成功？:"+flag);

    }
}
