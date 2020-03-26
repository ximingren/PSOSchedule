package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.Permission;
import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.po.User;
import com.ximingren.CourseSchedule.Dao.StudentInfoDao;
import com.ximingren.CourseSchedule.Dao.TeacherInfoDao;
import com.ximingren.CourseSchedule.Dao.UserDao;
import com.ximingren.CourseSchedule.Services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/20 21:32
 */
@Service
public class UserService implements IUserService {
    @Autowired
    UserDao userDao;
    @Autowired
    StudentInfoDao studentInfoDao;
    @Autowired
    TeacherInfoDao teacherInfoDao;

    @Override
    public User login(String username, String password) {
        User user = userDao.findUser(username, password);
        if (user != null) {
            String role = user.getRole();
            List<Permission> permissions = userDao.findPermissions(role);
            user.setPermissions(permissions);
            return user;
        }
        return null;
    }

    @Override
    public User loginStudent(String username, String password) {
        StudentInfo studentInfo = studentInfoDao.findStudent(username, password);
        User user = null;
        if (studentInfo != null) {
            user = new User();
            user.setUsername(studentInfo.getStudentname());
            user.setCondition(studentInfo.getClassno());
            String role = studentInfo.getRole();
            List<Permission> permissions = userDao.findPermissions(role);
            user.setPermissions(permissions);
        }
        return user;
    }

    @Override
    public User loginTeacher(String username, String password) {
        TeacherInfo teacherInfo = teacherInfoDao.findTeacher(username, password);
        User user = null;
        if (teacherInfo != null) {
            user = new User();
            user.setUsername(teacherInfo.getTeachername());
            user.setCondition(teacherInfo.getTeacherno());
            String role = teacherInfo.getRole();
            List<Permission> permissions = userDao.findPermissions(role);
            user.setPermissions(permissions);
        }
        return user;
    }
}
