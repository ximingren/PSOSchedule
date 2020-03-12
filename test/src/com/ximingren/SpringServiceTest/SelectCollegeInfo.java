package com.ximingren.SpringServiceTest;

import com.ximingren.CourseSchedule.Bean.po.CollegeInfo;
import com.ximingren.CourseSchedule.CourseScheduleSystemApplication;
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
public class SelectCollegeInfo {
    @Autowired
    ICollegeInfoService collegeInfoService;
    @Test
    public void testCollegeInfo() {
        List<CollegeInfo> collegeInfoList = collegeInfoService.findCollegeInfo();
        for (CollegeInfo collegeInfo : collegeInfoList) {
            System.out.println(collegeInfo.toString());
        }
    }


}
