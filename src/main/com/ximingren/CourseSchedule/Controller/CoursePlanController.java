package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.IClassScheduleService;
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
public class CoursePlanController {
    @Autowired
    ICoursePlanService coursePlanService;
    @Autowired
    IClassScheduleService classScheduleService;

    @RequestMapping(value = "/queryCoursePlan", method = RequestMethod.POST)
    public ResultVO queryCoursePlan(@RequestBody QueryVO queryVO) {
        try {
            Map<String, Integer> pageParam = new HashMap<>();
            if (queryVO.getPageParam() != null) {
                pageParam = queryVO.getPageParam();
                Integer pageNo = pageParam.get("pageNo") * pageParam.get("pageSize");
                pageParam.put("pageNum", pageNo);
            }
            List<CoursePlan> coursePlanList = coursePlanService.selectAll(queryVO);
            if (coursePlanList != null && coursePlanList.size() > 0) {
                return ResultVO.ok("查询课表成功", coursePlanList, pageParam);
            }
            return ResultVO.faile("无课表");
        } catch (Exception e) {
            return ResultVO.faile("课表查询失败");
        }
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ResultVO queryCoursePlan(@RequestBody ClassTask classTask) {
        try {
            boolean flag = classScheduleService.executeSchedule(classTask);
            if (flag) {
                return ResultVO.ok("排课成功");
            } else {
                return ResultVO.ok("排课失败");
            }
        } catch (Exception e) {
            return ResultVO.faile("排课失败");
        }
    }
}
