package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import com.ximingren.CourseSchedule.Bean.vo.Particle;
import com.ximingren.CourseSchedule.Dao.ClassTaskDao;
import com.ximingren.CourseSchedule.Util.ClassScheduleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName ClassTaskService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/6 17:18
 */
@Service
public class ClassTaskService {
    @Autowired
    private ClassTaskDao classTaskDao;
    //随机数生成器
    private Random generator = new Random();



}
