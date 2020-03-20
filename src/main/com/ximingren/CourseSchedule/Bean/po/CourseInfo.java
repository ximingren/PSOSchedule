package com.ximingren.CourseSchedule.Bean.po;

/**
 * @ClassName ClassInfo
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/20 14:49
 */
public class CourseInfo {
    private Long id;
    private String courseno;
    private String coursename;
    private String courseattr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseno() {
        return courseno;
    }

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCourseattr() {
        return courseattr;
    }

    public void setCourseattr(String courseattr) {
        this.courseattr = courseattr;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "id=" + id +
                ", courseno='" + courseno + '\'' +
                ", coursename='" + coursename + '\'' +
                ", courseattr='" + courseattr + '\'' +
                '}';
    }
}
