package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.ClassInfo;
import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.IClassInfoService;
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
public class ClassInfoController {
    @Autowired
    IClassInfoService classInfoService;

    @RequestMapping(value = "/queryClassInfo", method = RequestMethod.POST)
    public ResultVO queryCollegeInfo() {
        try {
            List<ClassInfo> classInfoList = classInfoService.findClassInfo();
            if (classInfoList != null && classInfoList.size() > 0) {
                return ResultVO.ok("查询班级成功", classInfoList);
            }
            return ResultVO.faile("无班级信息");
        } catch (Exception e) {
            return ResultVO.faile("班级查询失败"+e);
        }
    }
}
