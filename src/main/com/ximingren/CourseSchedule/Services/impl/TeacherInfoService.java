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

    @Override
    public Boolean saveTeacherInfo(TeacherInfo teacherInfo) {
        Long id = teacherInfo.getId();
        try {
            //根据id是否为空来确定是插入操作还是更新操作
            if (id != null) {
                if (teacherInfoDao.updateByPrimaryKey(teacherInfo) > 0) {
                    return true;
                }
                return false;
            } else {
                if (teacherInfoDao.insert(teacherInfo) > 0) {
                    return true;
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteTeacherInfo(TeacherInfo teacherInfo) {
        if (teacherInfoDao.deleteByPrimaryKey(teacherInfo) > 0) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public int getCount() {
        return teacherInfoDao.getCount();
    }
}
