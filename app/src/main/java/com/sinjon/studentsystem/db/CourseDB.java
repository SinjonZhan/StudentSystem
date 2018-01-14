package com.sinjon.studentsystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建课程数据库 保存课程信息
 *
 * @作者 xinrong
 * @创建日期 2018/1/8 22:25
 */
public class CourseDB extends SQLiteOpenHelper {

    public CourseDB(Context context) {
        super(context, "courses.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table course(id integer primary key autoincrement, coursename text not null, credit double not null, teacher text not null, limitselect integer not null, selected integer not null, category text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table course");
        onCreate(db);
    }
}
