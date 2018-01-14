package com.sinjon.studentsystem.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.sinjon.studentsystem.R;
import com.sinjon.studentsystem.dao.CourseDao;
import com.sinjon.studentsystem.domain.Course;
import com.sinjon.studentsystem.util.MyConstants;
import com.sinjon.studentsystem.util.SpTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.sinjon.studentsystem.R.raw.course;

/**
 * 课程相关界面
 *
 * @作者 xinrong
 * @创建日期 2018/1/8 17:52
 */
public class CourseActivity extends Activity {

    private static final int LOADING = 0;
    private static final int FINISH = 1;
    public static List<Course> courseDatas = new ArrayList<>(); //保存所有课程信息
    private CourseDao dao; //操作课程数据库
    private RadioGroup radioGroup; //单选框组
    private TabHost tabHost; //多选菜单
    private MyAdapter adapter;
    private ProgressBar pb_load; //加载进度条
    private LinearLayout ll_data; //课程数据

    //UI处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    pb_load.setVisibility(View.VISIBLE);
                    ll_data.setVisibility(View.GONE);
                    break;
                case FINISH:
                    pb_load.setVisibility(View.GONE);
                    ll_data.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(); //初始化视图
        initData(); //初始化数据
        initEvent(); //初始化事件
    }

    /**
     * 视图初始化
     */
    private void initView() {

        setContentView(R.layout.activity_course);

        //得到TabHost对象实例
        tabHost = findViewById(R.id.tabhost_course);
        //调用TabHost.setup()
        tabHost.setup();
        //创建Tab标签
        tabHost.addTab(tabHost.newTabSpec("data").setIndicator("data")
                .setContent(R.id.fragment_data));
        tabHost.addTab(tabHost.newTabSpec("search").setIndicator("search")
                .setContent(R.id.fragment_search));
        tabHost.addTab(tabHost.newTabSpec("select").setIndicator("select")
                .setContent(R.id.fragment_select));

        //初始化控件
        radioGroup = findViewById(R.id.radiogroup);
        pb_load = findViewById(R.id.pb_loaddata);
        ll_data = findViewById(R.id.ll_data);

        ListView lv_data = findViewById(R.id.lv_course_data);
        adapter = new MyAdapter();
        lv_data.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        if (SpTools.getBoolean(getApplicationContext(), MyConstants.READCOURSE, false)) {
            courseDatas = dao.getAllCourseData();
        }
        super.onResume();
    }

    /**
     * 数据初始化
     */
    private void initData() {
        //初始化dao层
        dao = new CourseDao(getApplicationContext());

        //读取课程信息course.txt 只读一次
        //数据处理在线程中执行
        //从raw获取输入流
        //读取
        if (!SpTools.getBoolean(getApplicationContext(), MyConstants.READCOURSE, false)) {
            //由于要加载数据库中的课程信息 属于耗时操作
            //因此使用进度条来表示加载过程
            //开始先发送一条消息表示正在加载数据中
            handler.obtainMessage(LOADING).sendToTarget();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream inputStream = getResources().openRawResource(course);
                    getCourseDatas(inputStream);
                }
            }).start();


        } else {

            courseDatas = dao.getAllCourseData();
        }


    }

    /**
     * 事件初始化
     */
    private void initEvent() {
        //RadioGroup选项变换事件监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int currentTab = tabHost.getCurrentTab();
                switch (checkedId) {
                    case R.id.rb_data:
                        tabHost.setCurrentTabByTag("data");
                        break;
                    case R.id.rb_search:
                        tabHost.setCurrentTabByTag("search");
                        break;
                    case R.id.rb_select:
                        tabHost.setCurrentTabByTag("select");

                        break;

                }
                //刷新menu
                getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            }
        });
    }

    public List<Course> getCourseDatas(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String line;
        if (inputStreamReader != null) {
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                while ((line = reader.readLine()) != null) {

                    String[] courseData = line.split(" ");
                    //将信息封装到一个Course实例中
                    Course course = new Course();
                    course.setCourseName(courseData[0]);
                    course.setCredit(courseData[1]);
                    course.setTeacher(courseData[2]);
                    course.setLimitSelect(courseData[3]);
                    course.setSelected(courseData[4]);
                    course.setCategory(courseData[5]);

                    //将该封装类数据信息添加到课程数据库中
                    dao.addCourseData(course);

                    courseDatas.add(course);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            SpTools.putBoolean(getApplicationContext(), MyConstants.READCOURSE, true);
            //发送一条消息表示加载数据库完成
            handler.obtainMessage(FINISH).sendToTarget();

        }

        return courseDatas;
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return courseDatas.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_lv_data, null);
                viewHolder = new ViewHolder();
                viewHolder.courseName = convertView.findViewById(R.id.tv_item_coursename);
                viewHolder.credit = convertView.findViewById(R.id.tv_item_credit);
                viewHolder.teacher = convertView.findViewById(R.id.tv_item_teacher);
                viewHolder.limitSelect = convertView.findViewById(R.id.tv_item_limitselect);
                viewHolder.selected = convertView.findViewById(R.id.tv_item_selected);
                viewHolder.category = convertView.findViewById(R.id.tv_item_category);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position == 0) {
                viewHolder.courseName.setText("课程信息");
                viewHolder.credit.setText("学分");
                viewHolder.teacher.setText("教师");
                viewHolder.limitSelect.setText("限选");
                viewHolder.selected.setText("已选");
                viewHolder.category.setText("类型");
            } else {
                //逐个将CourseDatas中的所有课程信息显示在列表中
                Course course = courseDatas.get(position - 1);

                viewHolder.courseName.setText(course.getCourseName());
                viewHolder.credit.setText(course.getCredit());
                viewHolder.teacher.setText(course.getTeacher());
                viewHolder.limitSelect.setText(course.getLimitSelect());
                viewHolder.selected.setText(course.getSelected());
                viewHolder.category.setText(course.getCategory());
            }

            adapter.notifyDataSetChanged();
            return convertView;
        }
    }

    class ViewHolder {
        TextView courseName; //课程名称
        TextView credit; //学分
        TextView teacher; //教师
        TextView limitSelect; //限选
        TextView selected; //已选
        TextView category; //类型
    }


}
