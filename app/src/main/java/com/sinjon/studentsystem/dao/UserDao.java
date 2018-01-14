package com.sinjon.studentsystem.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sinjon.studentsystem.db.UserDB;
import com.sinjon.studentsystem.domain.User;
import com.sinjon.studentsystem.domain.UserTable;
import com.sinjon.studentsystem.util.MyConstants;
import com.sinjon.studentsystem.util.SpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * dao层 操作用户数据库
 *
 * @作者 xinrong
 * @创建日期 2018/1/7 21:18
 */
public class UserDao {
    private UserDB userDB; //用户数据库
    private List<User> UserDatas = new ArrayList<>(); //保存所有用户信息
    private String userName;

    public UserDao(Context context) {
        userDB = new UserDB(context);
        //获取用户名
        userName = SpTools.getString(context, MyConstants.USER, "");
    }

    //该数据库存放用户注册信息
    //需要添加和查询操作


    /**
     * 添加用户信息
     *
     * @param user 用户信息的封装
     */
    public void addUserData(User user) {
        //打开数据库并添加用户信息
        SQLiteDatabase db = userDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.USERNAME, user.getUserName());
        values.put(UserTable.PASSWORD, user.getPasswd());
        values.put(UserTable.ISSELECTED, user.getIsSelected() + "");
        long id = db.insert(UserTable.TABLE, null, values);
        user.setId(id);

        //关闭数据库
        db.close();
    }

    public List<User> getAllUserData() {
        //读数据库获取所有注册用户信息
        SQLiteDatabase db = userDB.getReadableDatabase();
        Cursor cursor = db.query(UserTable.TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setUserName(cursor.getString(1));
            user.setPasswd(cursor.getString(2));

            //将该用户添加到用户列表中
            UserDatas.add(user);
        }


        //关闭游标和数据库
        cursor.close();
        db.close();
        return UserDatas;
    }

    /**
     * 更新用户数据库中isSelect该列的值为'y'
     * 'y'代表已选课
     */
    public void updateIsSelected() {
        SQLiteDatabase db = userDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.ISSELECTED, 'y' + "");
        db.update(UserTable.TABLE, values, "username=?", new String[]{userName});

        //关闭
        db.close();
    }

    /**
     * 获取用户数据库中isSelect列的值
     *
     * @return
     */
    public String getIsSelected() {
        String res = "";
        SQLiteDatabase db = userDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + UserTable.ISSELECTED + " from " + UserTable.TABLE + " where username=?", new String[]{userName});

        if (cursor.moveToNext()) {
            res = cursor.getString(0);

        }

        //关闭
        cursor.close();
        db.close();
        return res;
    }

    /**
     * 更新用户数据库中selectedCourses该列的值
     */
    public void updateSelectedCourses(String courses) {
        SQLiteDatabase db = userDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.SELECTEDCOURSES, courses);
        db.update(UserTable.TABLE, values, "username=?", new String[]{userName});

        //关闭
        db.close();
    }

    /**
     * 获取用户数据库中selectedCourses该列的值
     *
     * @return
     */
    public String getSelectedCourses() {
        String res = "";
        SQLiteDatabase db = userDB.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + UserTable.SELECTEDCOURSES + " from " + UserTable.TABLE + " where username=?", new String[]{userName});
        if (cursor.moveToNext()) {
            res = cursor.getString(0);

        }

        //关闭
        cursor.close();
        db.close();
        return res;
    }
}
