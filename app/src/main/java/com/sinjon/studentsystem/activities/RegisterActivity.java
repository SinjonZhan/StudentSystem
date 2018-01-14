package com.sinjon.studentsystem.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sinjon.studentsystem.R;
import com.sinjon.studentsystem.dao.UserDao;
import com.sinjon.studentsystem.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户注册界面
 *
 * @作者 xinrong
 * @创建日期 2018/1/7 12:23
 */
public class RegisterActivity extends Activity {

    private EditText et_user; //用户名
    private EditText et_pwd; //密码
    private EditText et_check_pwd; //密码确定
    private TextView tv_cancel; //取消按钮
    private TextView tv_yes; //确认按钮

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
     * 数据初始化
     */
    private void initData() {

        //初始化dao层类
        dao = new UserDao(getApplicationContext());
        //获取数据库中所有用户信息
        userDatas = dao.getAllUserData();

    }

    /**
     * 事件初始化
     */
    private void initEvent() {
        //事件监听

        //取消按钮监听
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //直接返回
            }
        });

        //确认按钮监听
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取注册信息
                String userName = et_user.getText().toString();
                String passwd = et_pwd.getText().toString();
                String check_passwd = et_check_pwd.getText().toString();

                //注册信息格式检查
                //1.各项不能为空
                //2.用户名不能重复
                //3.判断密码和确认密码是否相同
                //4.注册信息格式正确,将信息导入数据库存储
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passwd) || TextUtils.isEmpty(check_passwd)) {
                    Toast.makeText(getApplicationContext(), "注册信息各项都不能为空", Toast.LENGTH_SHORT).show();

                } else if (isUserNameExist(userName)) {
                    //判断用户名是否已存在
                    Toast.makeText(getApplicationContext(), "该用户名已存在", Toast.LENGTH_SHORT).show();

                } else if (!passwd.equals(check_passwd)) {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();

                    //不相同就清空密码项
                    et_pwd.setText("");
                    et_check_pwd.setText("");

                } else {
                    //将该用户信息添加到数据库
                    User user = new User();
                    user.setUserName(userName);
                    user.setPasswd(passwd);
                    user.setIsSelected('n');
                    dao.addUserData(user);


                    AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("\t注册成功") //设置对话框的标题
                            .setMessage("欢迎登录广工选课系统!") //设置对话框的内容
                            //设置对话框的按钮
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create();
                    dialog.show();

                }


            }
        });
    }

    /**
     * 判断是否已存在该用户名
     *
     * @param userName 申请的用户名
     * @return
     */
    private boolean isUserNameExist(String userName) {

        for (User user :
                userDatas) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 视图初始化
     */
    private void initView() {
        setContentView(R.layout.activity_register);

        //初始化控件
        et_user = findViewById(R.id.et_user);
        et_pwd = findViewById(R.id.et_pwd);
        et_check_pwd = findViewById(R.id.et_check_pwd);

        tv_cancel = findViewById(R.id.tv_cancel);
        tv_yes = findViewById(R.id.tv_yes);
    }
}
