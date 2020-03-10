package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Dao.TeacherInfoDao;
import com.ximingren.CourseSchedule.Services.ITeacherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TeacherInfoService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 16:33
 */
@Service
public class TeacherInfoService implements ITeacherInfoService {
    @Autowired
    TeacherInfoDao teacherInfoDao;
    @Override
    public List<TeacherInfo> queryTeacherInfo(QueryVO queryVO) {
        List<TeacherInfo> teacherInfoList = teacherInfoDao.selectAll(queryVO);
        return teacherInfoList;
    }
}
