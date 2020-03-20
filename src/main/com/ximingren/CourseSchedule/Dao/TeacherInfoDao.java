package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherInfoDao {
    //查询教师
    List<TeacherInfo> selectAll(QueryVO queryVO);

    //新增教师信息
    int insert(TeacherInfo teacherInfo);

    //更新教师信息
    int updateByPrimaryKey(TeacherInfo teacherInfo);

    //删除教师信息
    int deleteByPrimaryKey(TeacherInfo teacherInfo);

    //获取数量
    int getCount();
}

