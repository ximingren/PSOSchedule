package com.ximingren.SpringServiceTest;

import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.CourseScheduleSystemApplication;
import com.ximingren.CourseSchedule.Services.IStudentInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @ClassName SelectStudentInfo
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/11 14:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseScheduleSystemApplication.class)
public class SelectStudentInfo {
    @Autowired
    IStudentInfoService studentInfoService;

    @Test
    public void testByStudentNo() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCondition("201501010100");
        List<StudentInfo> studentInfoList = studentInfoService.queryStudentInfo(queryVO);
        for (StudentInfo studentInfo : studentInfoList) {
            System.out.println(studentInfo.toString());
        }
    }

    @Test
    public void testByStudentName() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCondition("李");
        List<StudentInfo> studentInfoList = studentInfoService.queryStudentInfo(queryVO);
        for (StudentInfo studentInfo : studentInfoList) {
            System.out.println(studentInfo.toString());
        }
    }

    @Test
    public void testInsert() {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setStudentno("201621190615");
        studentInfo.setCollegeno("01");
        studentInfo.setClassno("2016211906");
        boolean flag = studentInfoService.saveStudentInfo(studentInfo);
        System.out.println("保存成功？：" + flag);
    }

    @Test
    public void testDelete() {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setId((long) 1873);
        boolean flag = studentInfoService.deleteStudentInfo(studentInfo);
        System.out.println("删除成功？：" + flag);
    }
}

