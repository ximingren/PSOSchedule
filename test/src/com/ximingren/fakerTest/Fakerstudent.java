package com.ximingren.fakerTest;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Random;

/**
 * @ClassName Fakerstudent
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/11 13:48
 */
public class Fakerstudent {
    Faker faker = new Faker(new Locale("zh-CN"));
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/schedule?useUnicode=true&characterEncoding=utf-8&useSSL=false", "root", "root");
        } catch (Exception e) {
            System.out.println("连接数据库错误" + e);
        }
    }

    private String fakerStudentName() {
        String name = faker.name().fullName();
        return name;
    }

    private void insertFakerStudentInfo(String classNo, int studentNumber) {
        System.out.println(classNo);
        for (int i = 0; i < studentNumber; i++) {
            String studentNo = classNo;
            if (i < 10) {
                studentNo = studentNo + "0" + i;
            } else {
                studentNo = studentNo + i;
            }
            String studenName = fakerStudentName();
            String collegeNo = classNo.substring(4, 6);
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO student_info(studentNo,studentName,collegeNo,classNo) VALUES(?,?,?,?)");
                ps.setString(1, studentNo);
                ps.setString(2, studenName);
                ps.setString(3, collegeNo);
                ps.setString(4, classNo);
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Fakerstudent fakerstudent = new Fakerstudent();
        fakerstudent.fakerStudentName();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM class_info");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String classNo = resultSet.getString("classNo");
                int studentNumber = resultSet.getInt("studentNumber");
                fakerstudent.insertFakerStudentInfo(classNo, studentNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
