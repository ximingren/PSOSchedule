package com.ximingren.CourseSchedule.Bean.po;

public class TeacherInfo {
    private Long id;

    private String teacherno;

    private String teachername;

    private String collegeno;

    private Integer age;

    private String title;
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

    public String getTeacherno() {
        return teacherno;
    }

    public void setTeacherno(String teacherno) {
        this.teacherno = teacherno;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getCollegeno() {
        return collegeno;
    }

    public void setCollegeno(String collegeno) {
        this.collegeno = collegeno;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TeacherInfo{" +
                "id=" + id +
                ", teacherno='" + teacherno + '\'' +
                ", teachername='" + teachername + '\'' +
                ", collegeno='" + collegeno + '\'' +
                ", age=" + age +
                ", title='" + title + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}