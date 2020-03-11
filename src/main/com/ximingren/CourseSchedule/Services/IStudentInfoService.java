package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;

import java.util.List;

public interface IStudentInfoService {
    List<StudentInfo> queryStudentInfo(QueryVO queryVO);

    Boolean saveTeacherInfo(StudentInfo studentInfo);

    Boolean deleteTeacherInfo(StudentInfo studentInfo);
}
