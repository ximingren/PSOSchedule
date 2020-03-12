package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.CollegeInfo;
import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.ICollegeInfoService;
import com.ximingren.CourseSchedule.Services.ICoursePlanService;
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
@RestController()
public class CollegeController {
    @Autowired
    ICollegeInfoService collegeInfoService;

    @RequestMapping(value = "/queryCollegeInfo", method = RequestMethod.POST)
    public ResultVO queryCollegeInfo() {
        try {
            List<CollegeInfo> collegeInfoList = collegeInfoService.findCollegeInfo();
            if (collegeInfoList != null && collegeInfoList.size() > 0) {
                return ResultVO.ok("查询学院成功", collegeInfoList);
            }
            return ResultVO.faile("无学院信息");
        } catch (Exception e) {
            return ResultVO.faile("学院查询失败");
        }
    }
}
