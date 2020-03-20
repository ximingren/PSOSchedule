package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.ClassInfo;
import com.ximingren.CourseSchedule.Bean.po.CourseInfo;

import java.util.List;

public interface ICourseInfoService {
    List<CourseInfo> findCourseInfo();
}
