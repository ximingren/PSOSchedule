package com.ximingren.jdbcDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ClassTaskJDBC
 * @Description 连接数据库，获取排课计划
 * @Author ximingren
 * @Date 2020/2/28 17:49
 */
public class ClassTaskJDBC {
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/schedule?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "root");
        } catch (Exception e) {
            System.out.println("连接数据库错误" + e);
        }
    }

    public static List<ClassTask> selectClassTask() {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `schedule`.`class_task` WHERE `semester` = '2015-2016-1'");
            ResultSet resultSet = ps.executeQuery();
            List<ClassTask> classTaskList = new ArrayList<>();
            while (resultSet.next()) {
                ClassTask classTask = new ClassTask();
                classTask.setClassno(resultSet.getString("classNo"));
                classTask.setSemester(resultSet.getString("semester"));
                classTask.setCollegeno(resultSet.getString("collegeNo"));
                classTask.setCourseno(resultSet.getString("courseNo"));
                classTask.setTeacherno(resultSet.getString("teacherNo"));
                classTask.setCourseattr(resultSet.getString("courseAttr"));
                classTask.setStudentnumber(resultSet.getInt("studentNumber"));
                classTask.setWeekssum(resultSet.getInt("weeksSum"));
                classTask.setWeeksnumber(resultSet.getInt("weeksNumber"));
                classTask.setIsfix(resultSet.getString("isFix"));
                classTask.setClasstime(resultSet.getString("classTime"));
                classTaskList.add(classTask);
            }
            return classTaskList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> selectDistinctClassNo() {
        try {
            PreparedStatement ps = connection.prepareStatement("select distinct classNo from class_task;");
            ResultSet resultSet = ps.executeQuery();
            List<String> classNo = new ArrayList<>();
            while (resultSet.next()) {
                classNo.add(resultSet.getString("classNo"));
            }
            return classNo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        selectDistinctClassNo();
    }
}
