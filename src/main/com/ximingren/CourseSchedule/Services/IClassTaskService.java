package com.ximingren.CourseSchedule.Services;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;

import java.util.List;

/**
 * @ClassName IClassTaskService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 17:45
 */
public interface IClassTaskService {
    List<ClassTask> queryClassTask(QueryVO queryVO);
}
