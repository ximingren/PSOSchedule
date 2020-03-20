package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;

import java.util.List;

public interface IStudentInfoService {
    List<StudentInfo> queryStudentInfo(QueryVO queryVO);

    Boolean saveStudentInfo(StudentInfo studentInfo);

    Boolean deleteStudentInfo(StudentInfo studentInfo);

    int getCount();
}
