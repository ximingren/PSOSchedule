package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import com.ximingren.CourseSchedule.Dao.StudentInfoDao;
import com.ximingren.CourseSchedule.Services.IStudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName StudentInfoService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/11 14:49
 */
@Service
public class StudentInfoService implements IStudentInfoService {
    @Autowired
    StudentInfoDao studentInfoDao;

    @Override
    public List<StudentInfo> queryStudentInfo(QueryVO queryVO) {
        List<StudentInfo> teacherInfoList = studentInfoDao.selectAll(queryVO);
        return teacherInfoList;
    }

    @Override
    public Boolean saveTeacherInfo(StudentInfo studentInfo) {
        Long id = studentInfo.getId();
        try {
            //根据id是否为空来确定是插入操作还是更新操作
            if (id != null) {
                if (studentInfoDao.updateByPrimaryKey(studentInfo) > 0) {
                    return true;
                }
                return false;
            } else {
                if (studentInfoDao.insert(studentInfo) > 0) {
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
    public Boolean deleteTeacherInfo(StudentInfo studentInfo) {
        if (studentInfoDao.deleteByPrimaryKey(studentInfo) > 0) {
            return true;
        } else {
            return false;
        }
    }
}
