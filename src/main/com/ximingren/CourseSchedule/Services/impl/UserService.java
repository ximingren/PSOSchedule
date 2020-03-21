package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.Permission;
import com.ximingren.CourseSchedule.Bean.po.User;
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
}
