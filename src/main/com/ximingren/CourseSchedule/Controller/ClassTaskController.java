package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.IClassTaskService;
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
public class ClassTaskController {
    @Autowired
    IClassTaskService classTaskService;

    @RequestMapping(value = "/queryClassTask", method = RequestMethod.POST)
    public ResultVO queryClassTask(@RequestBody QueryVO queryVO) {
        try {
            Map<String, Integer> pageParam = new HashMap<>();
            if (queryVO.getPageParam() != null) {
                pageParam = queryVO.getPageParam();
                Integer pageNo = pageParam.get("pageNo") * pageParam.get("pageSize");
                pageParam.put("pageStart", pageNo);
            }
            List<ClassTask> classTaskList = classTaskService.queryClassTask(queryVO);
            if (classTaskList != null && classTaskList.size() > 0) {
                return ResultVO.ok("查询开课计划成功", classTaskList, pageParam);
            }
            return ResultVO.faile("无开课计划信息");
        } catch (Exception e) {
            return ResultVO.faile("开课计划查询失败");
        }
    }

    @RequestMapping("/deleteClassTask")
    public ResultVO deleteLocationInfo(@RequestBody ClassTask classTask) {
        try {
            if (classTaskService.deleteClassTask(classTask)) {
                return ResultVO.ok("开课计划删除成功");
            }
            return ResultVO.faile("开课计划删除失败");
        } catch (Exception e) {
            return ResultVO.faile("开课计划删除失败");
        }
    }

    @RequestMapping("/saveClassTask")
    public ResultVO saveLocationInfo(@RequestBody ClassTask classTask) {
        try {
            if (classTaskService.saveClassTask(classTask)) {
                return ResultVO.ok("开课计划保存成功");
            }
            return ResultVO.faile("开课计划保存失败");
        } catch (Exception e) {
            return ResultVO.faile("开课计划保存失败");
        }
    }
}
