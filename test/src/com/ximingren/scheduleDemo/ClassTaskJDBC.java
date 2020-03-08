package com.ximingren.scheduleDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.po.ClassroomLocation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static List<String> selectDistinctName(String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("select distinct " + name + " from class_task;");
            ResultSet resultSet = ps.executeQuery();
            List<String> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(resultSet.getString(name));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String selectTeachBuildNo(String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("select teachBuildNo from location_info where collegeNo =" + name);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getString("teachBuildNo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ClassroomLocation> selectClassRoomLocation(String teachBuildNo) {
        try {
            PreparedStatement ps = connection.prepareStatement("select id, teachBuildNo, classroomNo, capacity, classroomAttr from classroom_location where teachBuildNo=" + teachBuildNo);
            ResultSet resultSet = ps.executeQuery();
            List<ClassroomLocation> classTaskList = new ArrayList<>();
            while (resultSet.next()) {
                ClassroomLocation classroomLocation = new ClassroomLocation();
                classroomLocation.setId(resultSet.getLong("id"));
                classroomLocation.setTeachbuildno(resultSet.getString("teachBuildNo"));
                classroomLocation.setClassroomno(resultSet.getString("classroomNo"));
                classroomLocation.setCapacity(resultSet.getInt("capacity"));
                classroomLocation.setClassroomattr(resultSet.getString("classroomAttr"));
                classTaskList.add(classroomLocation);
            }
            return classTaskList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int selectStudentNumber(String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("select studentNumber  from class_info where classNo = " + name
            );
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getInt("studentNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static String selectCourseName(String name) {
        try {
            PreparedStatement ps = connection.prepareStatement("select id, courseNo, courseName, courseAttr from course_info where courseNo= " + name
            );
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getString("courseName");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
