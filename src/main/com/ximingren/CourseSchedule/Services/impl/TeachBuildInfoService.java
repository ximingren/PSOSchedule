package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.TeachBuildInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Dao.TeachBuildInfoDao;
import com.ximingren.CourseSchedule.Services.ITeachBuildInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName TeachBuildInfoService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 18:04
 */
@Service
public class TeachBuildInfoService implements ITeachBuildInfoService {

    @Autowired
    TeachBuildInfoDao teachBuildInfoDao;
    @Override
    public List<TeachBuildInfo> queryTeachBuildInfo(QueryVO queryVO) {
        try {
            List<TeachBuildInfo> teachBuildInfoList = teachBuildInfoDao.selectAll(queryVO);
            return teachBuildInfoList;
        }catch (Exception e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
