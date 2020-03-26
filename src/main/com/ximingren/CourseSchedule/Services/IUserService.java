package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.User;

public interface IUserService {
    User login(String username, String password);

    User loginStudent(String username, String password);
    User loginTeacher(String username, String password);
}
