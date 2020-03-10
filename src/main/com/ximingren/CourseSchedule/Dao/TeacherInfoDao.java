package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.TeacherInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherInfoDao {
    List<TeacherInfo> selectAll(QueryVO queryVO);
}

