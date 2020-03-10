package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import com.ximingren.CourseSchedule.Bean.vo.Particle;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Dao.ClassTaskDao;
import com.ximingren.CourseSchedule.Services.IClassTaskService;
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
public class ClassTaskService implements IClassTaskService {
    @Autowired
    ClassTaskDao classTaskDao;

    @Override
    public List<ClassTask> queryClassTask(QueryVO queryVO) {
        try {
            List<ClassTask> classTaskList = classTaskDao.selectAll(queryVO);
            return classTaskList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
