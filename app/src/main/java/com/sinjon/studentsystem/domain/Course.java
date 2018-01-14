package com.sinjon.studentsystem.domain;

/**
 * 课程封装类
 *
 * @作者 xinrong
 * @创建日期 2018/1/8 21:59
 */
public class Course {
    long id; //课程id
    private String courseName; //课程名称
    private String credit; //课程学分
    private String teacher; //课程教师
    private String limitSelect; //限选
    private String selected; //已选
    private String category; //课程类别
    private boolean isChecked = false; //是否被选中

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLimitSelect() {
        return limitSelect;
    }

    public void setLimitSelect(String limitSelect) {
        this.limitSelect = limitSelect;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", credit='" + credit + '\'' +
                ", teacher='" + teacher + '\'' +
                ", limitSelect='" + limitSelect + '\'' +
                ", selected='" + selected + '\'' +
                ", category='" + category + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
