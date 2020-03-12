package com.ximingren.CourseSchedule.Controller;

import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.po.TeachBuildInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Bean.vo.ResultVO;
import com.ximingren.CourseSchedule.Services.ICoursePlanService;
import com.ximingren.CourseSchedule.Services.ITeachBuildInfoService;
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
public class TeachBuildController {
    @Autowired
    ITeachBuildInfoService teachBuildInfoService;

    @RequestMapping(value = "/queryTeachBuild", method = RequestMethod.POST)
    public ResultVO queryTeachBuild(@RequestBody QueryVO queryVO) {
        try {
            Map<String, Integer> pageParam = new HashMap<>();
            if (queryVO.getPageParam() != null) {
                pageParam = queryVO.getPageParam();
                Integer pageNo = pageParam.get("pageNo") * pageParam.get("pageSize");
                pageParam.put("pageNum", pageNo);
            }
            List<TeachBuildInfo> teachBuildInfoList = teachBuildInfoService.queryTeachBuildInfo(queryVO);
            if (teachBuildInfoList != null && teachBuildInfoList.size() > 0) {
                return ResultVO.ok("查询教学楼成功", teachBuildInfoList, pageParam);
            }
            return ResultVO.faile("无教学楼");
        } catch (Exception e) {
            return ResultVO.faile("教学楼查询失败");
        }
    }
}
