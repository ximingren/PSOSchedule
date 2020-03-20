package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.ClassInfo;

import java.util.List;

public interface IClassInfoService {
    List<ClassInfo> findClassInfo();
}
