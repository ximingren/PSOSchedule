package com.ximingren.CourseSchedule.Bean.po;

/**
 * @ClassName ClassInfo
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/20 14:49
 */
public class ClassInfo {
    private Long id;
    private String classno;
    private String classname;
    private String studentnumber;

    @Override
    public String toString() {
        return "ClassInfo{" +
                "id=" + id +
                ", classno='" + classno + '\'' +
                ", classname='" + classname + '\'' +
                ", studentnumber='" + studentnumber + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getStudentnumber() {
        return studentnumber;
    }

    public void setStudentnumber(String studentnumber) {
        this.studentnumber = studentnumber;
    }
}
