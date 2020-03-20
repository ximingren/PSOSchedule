package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.StudentInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentInfoDao {
    List<StudentInfo> selectAll(QueryVO queryVO);

    //新增学生信息
    int insert(StudentInfo studentInfo);

    //更新学生信息
    int updateByPrimaryKey(StudentInfo studentInfo);

    //删除学生信息
    int deleteByPrimaryKey(StudentInfo studentInfo);

    //获取数量
    int getCount();
}
