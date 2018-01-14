package com.sinjon.studentsystem.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建用户数据库 保存用户信息
 *
 * @作者 xinrong
 * @创建日期 2018/1/7 21:17
 */
public class UserDB extends SQLiteOpenHelper {
    public UserDB(Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(id integer primary key autoincrement, username text not null, password integer not null, isselected char not null, selectedcourses varchar[30])");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table user");
        onCreate(db);
    }
}
