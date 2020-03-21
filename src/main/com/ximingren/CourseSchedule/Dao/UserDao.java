package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.Permission;
import com.ximingren.CourseSchedule.Bean.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    User findUser(@Param("username") String username, @Param("password") String password);

    List<Permission> findPermissions(@Param("roleId") String roleId);

}
