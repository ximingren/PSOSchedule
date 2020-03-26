package com.ximingren.CourseSchedule.Bean.po;

import java.util.List;

/**
 * @ClassName User
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/20 21:21
 */
public class User {
    private long id;
    private String username;
    private String password;
    private String role;
    private String roleName;
    private String loginType;
    private String condition;
    private List<Permission> permissions;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", roleName='" + roleName + '\'' +
                ", loginType='" + loginType + '\'' +
                ", condition='" + condition + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
