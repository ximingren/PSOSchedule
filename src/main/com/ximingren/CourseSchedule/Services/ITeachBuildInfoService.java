package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.TeachBuildInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;

import java.util.List;

/**
 * @ClassName ITeachBuildInfoService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 18:01
 */
public interface ITeachBuildInfoService {
    List<TeachBuildInfo> queryTeachBuildInfo(QueryVO queryVO);

    int getCount();
}
