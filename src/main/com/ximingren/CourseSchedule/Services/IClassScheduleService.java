package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;

public interface IClassScheduleService {
    boolean executeSchedule(ClassTask classTask);
}
