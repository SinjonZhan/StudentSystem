package com.sinjon.studentsystem.domain;

/**
 * 注册用户
 *
 * @作者 xinrong
 * @创建日期 2018/1/7 21:40
 */
public class User {

    private long id; //用户id
    private String userName; //用户名
    private String passwd; //密码
    private char isSelected = 'n'; //是否进行过选课
    private String selectedCourses = ""; //选择的课程编号列表 编号_编号_编号...

    public String getSelectedCourses() {
        return selectedCourses;
    }

    public void setSelectedCourses(String selectedCourses) {
        this.selectedCourses = selectedCourses;
    }


    public char getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(char isSelected) {
        this.isSelected = isSelected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passwd='" + passwd + '\'' +
                ", isSelected=" + isSelected +
                ", selectedCourses='" + selectedCourses + '\'' +
                '}';
    }

}
