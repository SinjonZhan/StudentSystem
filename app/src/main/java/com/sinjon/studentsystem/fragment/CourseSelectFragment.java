package com.sinjon.studentsystem.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sinjon.studentsystem.R;
import com.sinjon.studentsystem.activities.CourseActivity;
import com.sinjon.studentsystem.dao.CourseDao;
import com.sinjon.studentsystem.dao.UserDao;
import com.sinjon.studentsystem.domain.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程界面碎片之一 个人选课
 *
 * @作者 xinrong
 * @创建日期 2018/1/9 17:01
 */
public class CourseSelectFragment extends Fragment {

    private static final int LOADING = 0; //进度加载中
    private static final int FINISH = 1; //加载完成
    public List<Course> isCheckedCourseDatas = new ArrayList<>(); //保存被选中选课信息
    private View view; //碎片视图
    private Button bt_commit; //提交按钮
    private HorizontalScrollView hsv_data; //横向滚轮
    private ListView lv_course_data; //选课信息列表
    private List<Course> courseDatas = new ArrayList<>(); //保存所有选课信息
    private MyAdapter adapter;
    private CourseDao dao; //操作课程数据库
    private UserDao userDao; //操作用户数据库
    private ProgressBar pb_load;
    private Button bt_start; //开始选课
    //UI处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    hsv_data.setVisibility(View.GONE);
                    pb_load.setVisibility(View.GONE);

                    break;
                case FINISH:
                    pb_load.setVisibility(View.GONE);
                    hsv_data.setVisibility(View.VISIBLE);

                    break;
                default:
                    break;

            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select, null);
        return view;
    }

    @Override
    public void onStart() {
        initView(); //初始化视图
        initData(); //初始化数据
        initEvent(); //初始化事件

        super.onStart();
    }


    /**
     * 视图初始化
     */
    private void initView() {

        handler.obtainMessage(LOADING).sendToTarget();
        //初始化控件
        bt_commit = view.findViewById(R.id.bt_commit);
        bt_start = view.findViewById(R.id.bt_start);

        hsv_data = view.findViewById(R.id.hsv_select_data);

        pb_load = view.findViewById(R.id.pb_select_loaddata);

        lv_course_data = view.findViewById(R.id.lv_fragment_select_course_data);
        adapter = new MyAdapter();
        lv_course_data.setAdapter(adapter);


    }

    /**
     * 数据初始化
     */
    private void initData() {
        dao = new CourseDao(getActivity());
        userDao = new UserDao(getActivity());
        //加载课程信息
        courseDatas = CourseActivity.courseDatas;

    }

    /**
     * 事件初始化
     */
    private void initEvent() {

        //事件监听
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.obtainMessage(FINISH).sendToTarget();
            }
        });

        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否已经选课
                if (userDao.getIsSelected().equals("y")) {
                    handler.obtainMessage(LOADING).sendToTarget();

                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("\t注意") //设置对话框的标题
                            .setMessage("您已经参与过选课!") //设置对话框的内容
                            .setCancelable(false)
                            //设置对话框的按钮
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    return;
                }

                //点击提交按钮
                //获取所有被选中的课程信息并保存
                isCheckedCourseDatas.clear();
                for (Course course :
                        courseDatas) {
                    if (course.isChecked()) {
                        //System.out.println("---------------被选中的---------------" + course);
                        isCheckedCourseDatas.add(course);
                    }
                }
                //判断选课信息是否符合要求
                if (isCheckedCourseDatas.size() >= 2) {


                    for (int i = 0; i < isCheckedCourseDatas.size(); i++) {
                        for (int j = i + 1; j < isCheckedCourseDatas.size(); j++) {
                            Course course1 = isCheckedCourseDatas.get(i);
                            Course course2 = isCheckedCourseDatas.get(j);
                            if (course1.getCourseName().equals(course2.getCourseName())) {
                                //不允许重复选择了同一门课
                                Toast.makeText(getActivity(), "不允许重复选择同一门课\n请重新选择", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    } //for end
                }

                double sumCredit = 0.0;
                for (Course course :
                        isCheckedCourseDatas) {
                    if (course.getLimitSelect().equals(course.getSelected())) {
                        Toast.makeText(getActivity(), "所选课程中存在课程名额不足\n请重新选择", Toast.LENGTH_LONG).show();
                        return;
                    }
                    sumCredit += Double.valueOf(course.getCredit());
                }

                if (sumCredit < 9) {
                    Toast.makeText(getActivity(), "所选课程总学分不足九分\n请继续补选", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //所选课程均符合要求
                    //数据库保存学生选课信息
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("\t选课成功") //设置对话框的标题
                            .setMessage("您的选课信息已保存!") //设置对话框的内容
                            //设置对话框的按钮
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    handler.obtainMessage(LOADING).sendToTarget();
                    //修改数据库的已选人数信息
                    StringBuilder courses = new StringBuilder();

                    for (Course course : isCheckedCourseDatas) {
                        courses.append("_" + course.getId());
                        course.setSelected((Integer.valueOf(course.getSelected()) + 1 + ""));
                        dao.updateCourseData(course.getId() + "", course.getSelected());
                        courseDatas = dao.getAllCourseData();
                        adapter.notifyDataSetChanged();
                    }

                    String res = "";
                    res = courses.substring(1);
                    //将学生选课信息保存在UserDB数据库中的selectedCourses列
                    userDao.updateSelectedCourses(res);

                }
                //更新用户数据库中isSelected列的值为'y'代表已经选课
                userDao.updateIsSelected();
            }
        });

        //事件监听
        lv_course_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //选课监听
                if (position >= 1) {
                    Course course = courseDatas.get(position - 1);
                    CheckBox cb_select = view.findViewById(R.id.cb_select);
                    if (!course.isChecked()) {
                        course.setChecked(true);
                        cb_select.setChecked(true);
                        courseDatas.set(position - 1, course);
                    } else {
                        course.setChecked(false);
                        cb_select.setChecked(false);
                        courseDatas.set(position - 1, course);
                    }
                    adapter.notifyDataSetChanged();

                }


            }
        });


    }


    /**
     * 适配器
     */
    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return courseDatas.size() + 1;
        }

        @Override
        public Course getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_lv_data, null);
                viewHolder = new ViewHolder();
                viewHolder.cb_select = convertView.findViewById(R.id.cb_select);
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

            viewHolder.cb_select.setVisibility(View.VISIBLE);

            if (position == 0) {
                viewHolder.cb_select.setVisibility(View.INVISIBLE);
                viewHolder.courseName.setText("课程信息");
                viewHolder.credit.setText("学分");
                viewHolder.teacher.setText("教师");
                viewHolder.limitSelect.setText("限选");
                viewHolder.selected.setText("已选");
                viewHolder.category.setText("类型");
            } else {
                //逐个将CourseDatas中的所有课程信息显示在列表中
                Course course = courseDatas.get(position - 1);
                viewHolder.cb_select.setChecked(course.isChecked());
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

        CheckBox cb_select; //单选框
        TextView courseName; //课程名称
        TextView credit; //学分
        TextView teacher; //教师
        TextView limitSelect; //限选
        TextView selected; //已选
        TextView category; //类型
    }
}
