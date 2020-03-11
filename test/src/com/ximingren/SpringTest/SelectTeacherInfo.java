package com.ximingren.SpringTest;

import com.ximingren.CourseSchedule.Bean.po.CollegeInfo;
import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.CourseScheduleSystemApplication;
import com.ximingren.CourseSchedule.Services.ICollegeInfoService;
import com.ximingren.CourseSchedule.Services.ITeacherInfoService;
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
public class SelectTeacherInfo {
    @Autowired
    ITeacherInfoService teacherInfoService;
    @Test
    public void testByCollege() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCollegeno("01");
        List<TeacherInfo> teacherInfoList = teacherInfoService.queryTeacherInfo(queryVO);
        for (TeacherInfo teacherInfo : teacherInfoList) {
            System.out.println(teacherInfo.toString());
        }
    }

    @Test
    public void testByVague() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCollegeno("01");
        queryVO.setCondition("李");
        List<TeacherInfo> teacherInfoList = teacherInfoService.queryTeacherInfo(queryVO);
        for (TeacherInfo teacherInfo : teacherInfoList) {
            System.out.println(teacherInfo.toString());
        }
    }

    @Test
    public void testInsert() {
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setTeacherno("123");
        teacherInfo.setTeachername("李先生");
        teacherInfo.setCollegeno("01");
        teacherInfo.setAge(12);
        teacherInfo.setTitle("讲师");
        boolean flag = teacherInfoService.saveTeacherInfo(teacherInfo);
        System.out.println("保存成功？："+flag);
    }

    @Test
    public void testDelete() {
        TeacherInfo teacherInfo = new TeacherInfo();
        teacherInfo.setId((long) 106);
        boolean flag = teacherInfoService.deleteTeacherInfo(teacherInfo);
        System.out.println("删除成功？："+flag);
    }
}
