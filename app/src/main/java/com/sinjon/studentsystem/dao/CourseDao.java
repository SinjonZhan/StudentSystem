package com.sinjon.studentsystem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinjon.studentsystem.db.CourseDB;
import com.sinjon.studentsystem.domain.Course;
import com.sinjon.studentsystem.domain.CourseTable;

import java.util.ArrayList;
import java.util.List;


/**
 * dao层 操作课程数据库
 *
 * @作者 xinrong
 * @创建日期 2018/1/8 22:26
 */
public class CourseDao {

    private CourseDB courseDB; //课程数据库
    private List<Course> courseDatas = new ArrayList<>(); //保存所有课程信息


    public CourseDao(Context context) {
        courseDB = new CourseDB(context);
    }

    //数据库操作

    /**
     * 向数据库中添加课程信息
     *
     * @param course
     */
    public void addCourseData(Course course) {
        //打开数据库添加课程信息
        SQLiteDatabase db = courseDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CourseTable.COURSENAME, course.getCourseName());
        values.put(CourseTable.CREDIT, course.getCredit());
        values.put(CourseTable.TEACHER, course.getTeacher());
        values.put(CourseTable.LIMITSELECT, course.getLimitSelect());
        values.put(CourseTable.SELECTED, course.getSelected());
        values.put(CourseTable.CATEGORY, course.getCategory());

        long id = db.insert(CourseTable.TABLE, null, values);
        course.setId(id);

        //关闭数据库
        db.close();
    }

    /**
     * 获取所有课程信息
     */
    public List<Course> getAllCourseData() {
        courseDatas.clear();
        SQLiteDatabase db = courseDB.getReadableDatabase();
        Cursor cursor = db.query(CourseTable.TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            //使用Course类来封装课程信息
            Course course = new Course();
            course.setId(cursor.getLong(0));
            course.setCourseName(cursor.getString(1));
            course.setCredit(cursor.getString(2));
            course.setTeacher(cursor.getString(3));
            course.setLimitSelect(cursor.getString(4));
            course.setSelected(cursor.getString(5));
            course.setCategory(cursor.getString(6));

            //将该课程添加到课程列表中
            courseDatas.add(course);

        }

        //关闭游标和数据库
        cursor.close();
        db.close();
        return courseDatas;
    }

    /**
     * 获取指定学分的课程信息
     */
    public List<Course> getAppointCreditCourseData(String search_credit) {
        courseDatas.clear();
        SQLiteDatabase db = courseDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CourseTable.TABLE + " where " + CourseTable.CREDIT + " =?", new String[]{search_credit});
        while (cursor.moveToNext()) {
            //使用Course类来封装课程信息
            Course course = new Course();
            course.setId(cursor.getLong(0));
            course.setCourseName(cursor.getString(1));
            course.setCredit(cursor.getString(2));
            course.setTeacher(cursor.getString(3));
            course.setLimitSelect(cursor.getString(4));
            course.setSelected(cursor.getString(5));
            course.setCategory(cursor.getString(6));

            //将该课程添加到课程列表中
            courseDatas.add(course);

        }

        //关闭游标和数据库
        cursor.close();
        db.close();
        return courseDatas;
    }

    /**
     * 获取指定学分的课程信息
     */
    public List<Course> getAppointCategoryCourseData(String search_category) {
        courseDatas.clear();
        SQLiteDatabase db = courseDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CourseTable.TABLE + " where " + CourseTable.CATEGORY + " like?", new String[]{"%" + search_category + "%"});
        while (cursor.moveToNext()) {
            //使用Course类来封装课程信息
            Course course = new Course();
            course.setId(cursor.getLong(0));
            course.setCourseName(cursor.getString(1));
            course.setCredit(cursor.getString(2));
            course.setTeacher(cursor.getString(3));
            course.setLimitSelect(cursor.getString(4));
            course.setSelected(cursor.getString(5));
            course.setCategory(cursor.getString(6));

            //将该课程添加到课程列表中
            courseDatas.add(course);

        }

        //关闭游标和数据库
        cursor.close();
        db.close();
        return courseDatas;
    }

    /**
     * 更新课程信息
     *
     * @param selected
     */
    public void updateCourseData(String id, String selected) {
        SQLiteDatabase db = courseDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CourseTable.SELECTED, selected);
        db.update(CourseTable.TABLE, values, "id=?", new String[]{id});

        db.close();
    }

    /**
     * 根据课程ID寻找到课程
     *
     * @param courses
     * @return
     */
    public List<Course> getCourseByUserSelectedCourses(String courses) {

        //清空
        courseDatas.clear();
        SQLiteDatabase db = courseDB.getReadableDatabase();

        //处理courses将id分离
        String[] ids = courses.split("_");

        //查询该用户所选的课程信息
        for (String id : ids) {
            Cursor cursor = db.rawQuery("select * from " + CourseTable.TABLE + " where id = ?", new String[]{id});

            if (cursor.moveToNext()) {
                //使用Course类来封装课程信息
                Course course = new Course();
                course.setId(cursor.getLong(0));
                course.setCourseName(cursor.getString(1));
                course.setCredit(cursor.getString(2));
                course.setTeacher(cursor.getString(3));
                course.setLimitSelect(cursor.getString(4));
                course.setSelected(cursor.getString(5));
                course.setCategory(cursor.getString(6));
                //将该课程添加到课程列表中
                courseDatas.add(course);
            }

            cursor.close();
        }

        //关闭
        db.close();
        return courseDatas;
    }
}
