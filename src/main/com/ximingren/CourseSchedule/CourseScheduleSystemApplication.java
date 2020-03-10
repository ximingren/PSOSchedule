package com.ximingren.CourseSchedule;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@CrossOrigin
@SpringBootApplication
@MapperScan("com.ximingren.CourseSchedule.Dao")
@EnableTransactionManagement
public class CourseScheduleSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseScheduleSystemApplication.class, args);
    }
}
