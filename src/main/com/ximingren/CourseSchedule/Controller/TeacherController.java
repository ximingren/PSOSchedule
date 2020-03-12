package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.CollegeInfo;
import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.ICollegeInfoService;
import com.ximingren.CourseSchedule.Services.ITeacherInfoService;
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
public class TeacherController {
    @Autowired
    ITeacherInfoService teacherInfoService;

    @RequestMapping(value = "/queryTeacherInfo", method = RequestMethod.POST)
    public ResultVO queryCollegeInfo(@RequestBody QueryVO queryVO) {
        try {
            Map<String, Integer> pageParam = new HashMap<>();
            if (queryVO.getPageParam() != null) {
                pageParam = queryVO.getPageParam();
                Integer pageNo = pageParam.get("pageNo") * pageParam.get("pageSize");
                pageParam.put("pageStart", pageNo);
            }
            List<TeacherInfo> teacherInfoList = teacherInfoService.queryTeacherInfo(queryVO);
            if (teacherInfoList != null && teacherInfoList.size() > 0) {
                return ResultVO.ok("查询教师成功", teacherInfoList, pageParam);
            }
            return ResultVO.faile("无教师信息");
        } catch (Exception e) {
            return ResultVO.faile("教师查询失败");
        }
    }

    @RequestMapping("/deleteTeacherInfo")
    public ResultVO deleteLocationInfo(@RequestBody TeacherInfo teacherInfo) {
        try {
            if (teacherInfoService.deleteTeacherInfo(teacherInfo)) {
                return ResultVO.ok("教师删除成功");
                //return ResultVO.ok("学院教学区域安排位置删除成功",locationInfoList,PageUtil.pageDataHelper(locationInfoList));
            }
            return ResultVO.faile("教师删除失败");
        } catch (Exception e) {
            return ResultVO.faile("教师删除失败");
        }
    }

    @RequestMapping("/saveTeacherInfo")
    public ResultVO saveLocationInfo(@RequestBody TeacherInfo teacherInfo) {
        try {
            if (teacherInfoService.saveTeacherInfo(teacherInfo)) {
                return ResultVO.ok("教师保存成功");
            }
            return ResultVO.faile("教师保存失败");
        } catch (Exception e) {
            return ResultVO.faile("教师保存失败");
        }
    }
}
