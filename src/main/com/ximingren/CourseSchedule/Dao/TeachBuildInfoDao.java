package com.ximingren.CourseSchedule.Dao;

import com.ximingren.CourseSchedule.Bean.po.TeachBuildInfo;
import com.ximingren.CourseSchedule.Bean.vo.QueryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName TeachBuildInfoDao
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/10 18:04
 */
@Mapper
public interface TeachBuildInfoDao {
    List<TeachBuildInfo> selectAll(QueryVO queryVO);

    int getCount();
}
