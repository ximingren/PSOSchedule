package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.IStudentInfoService;
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
public class StudentController {
    @Autowired
    IStudentInfoService studentInfoService;

    @RequestMapping(value = "/queryStudentInfo", method = RequestMethod.POST)
    public ResultVO queryCollegeInfo(@RequestBody QueryVO queryVO) {
        try {
            Map<String, Integer> pageParam = new HashMap<>();
            if (queryVO.getPageParam() != null) {
                pageParam = queryVO.getPageParam();
                Integer pageNo = pageParam.get("pageNo") * pageParam.get("pageSize");
                pageParam.put("pageStart", pageNo);
            }
            List<StudentInfo> studentInfoList = studentInfoService.queryStudentInfo(queryVO);
            if (studentInfoList != null && studentInfoList.size() > 0) {
                return ResultVO.ok("查询学生成功", studentInfoList, pageParam);
            }
            return ResultVO.faile("无学生信息");
        } catch (Exception e) {
            return ResultVO.faile("学生查询失败");
        }
    }

    @RequestMapping("/deleteStudentInfo")
    public ResultVO deleteStudentInfo(@RequestBody StudentInfo studentInfo) {
        try {
            if (studentInfoService.deleteStudentInfo(studentInfo)) {
                return ResultVO.ok("学生删除成功");
            }
            return ResultVO.faile("学生删除失败");
        } catch (Exception e) {
            return ResultVO.faile("学生删除失败");
        }
    }

    @RequestMapping("/saveStudentInfo")
    public ResultVO saveStudentInfo(@RequestBody StudentInfo studentInfo) {
        try {
            if (studentInfoService.saveStudentInfo(studentInfo)) {
                return ResultVO.ok("学生保存成功");
            }
            return ResultVO.faile("学生保存失败");
        } catch (Exception e) {
            return ResultVO.faile("学生保存失败");
        }
    }
}
