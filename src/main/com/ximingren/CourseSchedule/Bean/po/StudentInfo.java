package com.ximingren.CourseSchedule.Bean.po;

/**
 * @ClassName StudentInfo
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/11 14:31
 */
public class StudentInfo {
    private Long id;
    private String studentno;
    private String studentname;
    private String collegeno;
    private String classno;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentno() {
        return studentno;
    }

    public void setStudentno(String studentno) {
        this.studentno = studentno;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getCollegeno() {
        return collegeno;
    }

    public void setCollegeno(String collegeno) {
        this.collegeno = collegeno;
    }

    public String getClassno() {
        return classno;
    }

    public void setClassno(String classno) {
        this.classno = classno;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "id=" + id +
                ", studentno='" + studentno + '\'' +
                ", studentname='" + studentname + '\'' +
                ", collegeno='" + collegeno + '\'' +
                ", classno='" + classno + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
