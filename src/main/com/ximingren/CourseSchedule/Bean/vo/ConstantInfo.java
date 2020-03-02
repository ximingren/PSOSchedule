package com.ximingren.CourseSchedule.Bean.vo;

public class ConstantInfo {
    public static final String IS_FIX = "isFix";//是否固定
    public static final String COLLEGE_NO = "collegeNo";//学院编号
    public static final String CLASS_NO = "classNo";//班级编号
    public static final String TEACHER_NO = "teacherNo";//教师编号
    public static final String COURSE_NO = "courseNo";//课程编号
    public static final String COURSE_ATTR = "courseAttr";//课程属性
    public static final String CLASS_TIME = "classTime";//上课时间
    public static final String CLASSROOM_NO = "classroomNo";
    public static final String DEFAULT_CLASS_TIME = "00";//默认课程编码
    public static final String SEMESTER = "semester";//开课学期
    public static final String PROFESSIONAL_CODE = "01";//专业课码值
    public static final String ELECTIVE_CODE = "02";//选修课码值
    public static final String PHYSICAL_CODE = "03";//体育课码值
    public static final String MEDICAL_CODE = "04";//医学实验课码值
    public static final String CHEMISTRY_CODE = "05";//化学实验课码值
    public static final String DANCE_CODE = "06";//舞蹈实践课码值
    public static final String BROADCAST_CODE = "07";//播音实践课码值
    public static final String ELECTRICITY_CODE = "08";//电子实验课码值
    public static final String ART_CODE = "09";//美术实践课码值
    public static final String COMPUTER_CODE = "10";//计算机实验课码值
    public static final String MUSIC_PERFOMANCE_CODE = "11";//音乐表演课码值
    public static final String PHYSICAL_EXPERIMENT_CODE = "12";//物理实验课码值
    public static final int GENERATION = 100;//遗传代数
    public static final String[][] PROPESSIONAL_VALUE = new String[][]{
            {"01", "06", "11", "16", "21"},
            {"02", "07", "12", "17", "22"},
            {"03", "08", "13", "18", "23"},
            {"04", "09", "14", "19", "24"},
            {"05","10","15","20","25"}};//专业课期望值
    public static final String[][] EXPERIMENT_VALUE = new String[][]{
            {"04", "09", "14", "19"},
            {"05", "10", "15", "20", "25"},
            {"03", "08", "13", "18"},
            {"02", "07", "12", "17", "22"},
            {"01", "06", "11", "16", "21", "23", "24", "25"}};//实验课期望值
    public static final String[][] ELECTIVE_VALUE = new String[][]{
            {"03", "08", "13", "18", "23"},
            {"02", "07", "12", "17", "22"},
            {"01", "04", "14","06", "09", "11", "16", "19", "21", "24"},
            {"05", "10", "15", "20", "25"}
    };//选修课期望值
    public static final String[][] PHYSICAL_VALUE = new String[][]{
            {"04", "09", "14", "19"},
            {"03", "08", "13", "18"},
            {"02", "07", "12", "17", "22"},
            {"01", "05", "06", "10", "11", "15", "16", "20", "21", "23", "24", "25"}
    };//体育课期望值

}
