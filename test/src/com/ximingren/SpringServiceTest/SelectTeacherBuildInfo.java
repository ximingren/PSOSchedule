package com.ximingren.SpringServiceTest;

import com.ximingren.CourseSchedule.Bean.po.TeachBuildInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.CourseScheduleSystemApplication;
import com.ximingren.CourseSchedule.Services.ITeachBuildInfoService;
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
public class SelectTeacherBuildInfo {
    @Autowired
    ITeachBuildInfoService teachBuildInfoService;
    @Test
    public void testTeacherBuildInfo() {
        QueryVO queryVO = new QueryVO();
        queryVO.setCondition("号");
        List<TeachBuildInfo> teachBuildInfoList = teachBuildInfoService.queryTeachBuildInfo(queryVO);
        for (TeachBuildInfo teachBuildInfo : teachBuildInfoList) {
            System.out.println(teachBuildInfo.toString());
        }
    }


}
