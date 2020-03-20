package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.ClassInfo;
import com.ximingren.CourseSchedule.Dao.ClassInfoDao;
import com.ximingren.CourseSchedule.Services.IClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ClassInfoService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/20 14:51
 */
@Service
public class ClassInfoService implements IClassInfoService {
    @Autowired
    ClassInfoDao classInfoDao;

    @Override
    public List<ClassInfo> findClassInfo() {
        return classInfoDao.selectAll();
    }
}
