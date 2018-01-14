package com.sinjon.studentsystem.domain;

/**
 * 数据库用户表信息
 *
 * @作者 xinrong
 * @创建日期 2018/1/7 21:26
 */
public interface UserTable {
    String TABLE = "user"; //用户表表名
    String USERNAME = "username";
    String PASSWORD = "password";
    String ISSELECTED = "isselected"; //是否参加过选课
    String SELECTEDCOURSES = "selectedcourses"; //选择的课程编号列表字串
}
