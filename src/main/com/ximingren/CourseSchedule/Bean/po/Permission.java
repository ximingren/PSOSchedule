package com.ximingren.CourseSchedule.Bean.po;

/**
 * @ClassName Permission
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/21 11:01
 */
public class Permission {
    private long id;
    private String rolename;
    private String permission;
    private String roleId;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", rolename='" + rolename + '\'' +
                ", permission='" + permission + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}
