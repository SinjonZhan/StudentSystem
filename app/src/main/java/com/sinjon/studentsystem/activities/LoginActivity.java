package com.sinjon.studentsystem.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinjon.studentsystem.R;
import com.sinjon.studentsystem.dao.UserDao;
import com.sinjon.studentsystem.domain.User;
import com.sinjon.studentsystem.util.MyConstants;
import com.sinjon.studentsystem.util.SpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录界面
 *
 * @作者 xinrong
 * @创建日期 2018/1/7 11:23
 */
public class LoginActivity extends Activity {

    private EditText et_user; //用户名
    private EditText et_pwd; //密码
    private TextView tv_register; //注册按钮
    private TextView tv_login; //登录按钮

    private UserDao dao; //数据库操作类
    private List<User> userDatas = new ArrayList<>(); //保存所有用户信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(); //初始化视图
        initData(); //初始化数据
        initEvent(); //初始化事件
    }

    @Override
    protected void onResume() {
        userDatas = dao.getAllUserData();
        super.onResume();
    }

    /**
     * 事件初始化
     */
    private void initEvent() {
        //设置监听器

        //注册监听
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //前往注册界面
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //登录监听
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_user.getText().toString();
                String passwd = et_pwd.getText().toString();

                if (!isUserNamePasswdRight(userName, passwd)) {
                    Toast.makeText(getApplicationContext(), "用户名或密码不正确!", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "欢迎登录广工选课系统", Toast.LENGTH_SHORT).show();
                    SpTools.putString(getApplication(), MyConstants.USER, userName);
                    Intent intent = new Intent(LoginActivity.this, CourseActivity.class);
                    startActivity(intent);

                }
            }
        });
    }


    /**
     * 判断用户名和对应的密码是否正确
     *
     * @param userName
     * @param passwd
     * @return
     */
    private boolean isUserNamePasswdRight(String userName, String passwd) {
        for (User user :
                userDatas) {
            if (user.getUserName().equals(userName)) {
                if (user.getPasswd().equals(passwd)) {
                    return true;
                } else {
                    return false;
                }
            }
        }


        return false;
    }

    /**
     * 数据初始化
     */
    private void initData() {

        //初始化dao层类
        dao = new UserDao(getApplicationContext());
        //获取数据库中所有用户信息
        userDatas = dao.getAllUserData();
    }

    /**
     * 视图初始化
     */
    private void initView() {
        setContentView(R.layout.activity_login);

        //初始化控件
        et_user = findViewById(R.id.et_user);
        et_pwd = findViewById(R.id.et_pwd);

        tv_register = findViewById(R.id.tv_register);
        tv_login = findViewById(R.id.tv_login);

    }


}
