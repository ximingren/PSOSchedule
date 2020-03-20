package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;

import java.util.List;

public interface ITeacherInfoService {
    List<TeacherInfo> queryTeacherInfo(QueryVO queryVO);
    Boolean saveTeacherInfo(TeacherInfo teacherInfo);
    Boolean deleteTeacherInfo(TeacherInfo teacherInfo);

    int getCount();
}
