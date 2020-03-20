package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.ClassInfo;
import com.ximingren.CourseSchedule.Bean.po.CourseInfo;
import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.IClassInfoService;
import com.ximingren.CourseSchedule.Services.ICourseInfoService;
import com.ximingren.CourseSchedule.Services.IStudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CoursePlanController
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/11 17:43
 */
@RestController
public class CourseInfoController {
    @Autowired
    ICourseInfoService classInfoService;

    @RequestMapping(value = "/queryCourseInfo", method = RequestMethod.POST)
    public ResultVO queryCollegeInfo() {
        try {
            List<CourseInfo> courseInfoList = classInfoService.findCourseInfo();
            if (courseInfoList != null && courseInfoList.size() > 0) {
                return ResultVO.ok("查询课程成功", courseInfoList);
            }
            return ResultVO.faile("无课程信息");
        } catch (Exception e) {
            return ResultVO.faile("课程查询失败"+e);
        }
    }
}
