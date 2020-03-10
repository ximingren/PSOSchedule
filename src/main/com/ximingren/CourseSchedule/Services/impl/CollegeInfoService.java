package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.CollegeInfo;
import com.ximingren.CourseSchedule.Dao.CollegeInfoDao;
import com.ximingren.CourseSchedule.Services.ICollegeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName CollegeInfoService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 16:20
 */
@Service
public class CollegeInfoService implements ICollegeInfoService {
    @Autowired
    CollegeInfoDao collegeInfoDao;
    @Override
    public List<CollegeInfo> findCollegeInfo() {
        try {
            List<CollegeInfo> collegeInfoList = collegeInfoDao.selectAll();
            return collegeInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
