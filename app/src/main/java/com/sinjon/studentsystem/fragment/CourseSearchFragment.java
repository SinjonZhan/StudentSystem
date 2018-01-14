package com.sinjon.studentsystem.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sinjon.studentsystem.R;
import com.sinjon.studentsystem.dao.CourseDao;
import com.sinjon.studentsystem.dao.UserDao;
import com.sinjon.studentsystem.domain.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程界面碎片之一 课程搜索
 *
 * @作者 xinrong
 * @创建日期 2018/1/9 17:00
 */
public class CourseSearchFragment extends Fragment {

    private static final int SEARCH0 = 0; //查询内容1
    private static final int SEARCH1 = 1; //查询内容2:学分
    private static final int SEARCH2 = 2; //查询内容3:课程性质
    private static final int LOADING = 0; //进度加载中
    private static final int FINISH = 1; //加载完成
    private int et_search_data; //编辑框的搜索内容范围 由上面三种状态决定
    private PopupWindow pw; //弹出窗口
    private View view; //Fragment的布局
    private EditText et_data; //查询的内容
    private ImageButton ib_search; //查询按钮
    private Button bt_search; //查询内容选择按钮
    private ScaleAnimation sa; //缩放动画
    private View popupView; //弹出窗口
    private ListView lv_search_course_data; //保存查询到的所有课程信息列表
    private List<Course> courseDatas = new ArrayList<>(); //保存查询到的所有课程信息
    private MyAdapter adapter; //课程列表适配器
    private ProgressBar pb_load; //进度条
    private HorizontalScrollView hsv_data; //横向滚轮
    private CourseDao dao; //操作课程数据库
    private UserDao userDao; //操作用户数据库
    private TextView tv_nodata; //查询不到内容时显示此内容
    //UI处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    pb_load.setVisibility(View.VISIBLE);
                    hsv_data.setVisibility(View.GONE);
                    tv_nodata.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();

                    break;
                case FINISH:
                    pb_load.setVisibility(View.GONE);
                    hsv_data.setVisibility(View.VISIBLE);
                    tv_nodata.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
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

    /**
     * 视图初始化
     */
    private void initView() {
        //初始化弹出窗体
        initPopupWindow();

        //初始化控件
        et_data = view.findViewById(R.id.et_data);

        ib_search = view.findViewById(R.id.ib_search);

        bt_search = view.findViewById(R.id.bt_search);

        lv_search_course_data = view.findViewById(R.id.lv_fragment_search_course_data);
        adapter = new MyAdapter();
        lv_search_course_data.setAdapter(adapter);

        pb_load = view.findViewById(R.id.pb_fragment_search_loaddata);

        hsv_data = view.findViewById(R.id.hsv_search_data);

        tv_nodata = view.findViewById(R.id.tv_nodata);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        //初始化搜索内容范围
        et_search_data = SEARCH0;

        //初始化dao层
        dao = new CourseDao(getActivity());
        userDao = new UserDao(getActivity());
    }

    /**
     * 事件初始化
     */
    private void initEvent() {
        //事件监听
        ib_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击搜索按钮
                if (et_search_data == SEARCH0) {
                    tv_nodata.setVisibility(View.VISIBLE);
                    pb_load.setVisibility(View.GONE);
                    hsv_data.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "请先选择查询内容\n再进行搜索操作", Toast.LENGTH_SHORT).show();
                } else if (et_search_data == SEARCH1) {
                    //获取查询内容
                    String search_credit = et_data.getText().toString();
                    //获取指定学分的课程信息并存入courseDatas
                    courseDatas.clear();
                    courseDatas = dao.getAppointCreditCourseData(search_credit);
                    if (courseDatas.size() == 0) {
                        //System.out.println("---------------没有查询到内容---------------");
                        tv_nodata.setVisibility(View.VISIBLE);
                        pb_load.setVisibility(View.GONE);
                        hsv_data.setVisibility(View.GONE);
                    } else {
                        //System.out.println("---------------查询到的内容---------------" + courseDatas.size());
                        //显示加载课程信息进度条
                        handler.obtainMessage(LOADING).sendToTarget();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    //关闭加载信息的进度条
                                    handler.obtainMessage(FINISH).sendToTarget();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    }


                } else if (et_search_data == SEARCH2) {
                    //获取查询内容
                    String search_category = et_data.getText().toString();
                    //获取指定学分的课程信息并存入courseDatas
                    courseDatas.clear();
                    courseDatas = dao.getAppointCategoryCourseData(search_category);
                    if (courseDatas.size() == 0) {
                        //System.out.println("---------------没有查询到内容---------------");
                        tv_nodata.setVisibility(View.VISIBLE);
                        pb_load.setVisibility(View.GONE);
                        hsv_data.setVisibility(View.GONE);
                    } else {
                        //System.out.println("---------------查询到的内容---------------" + courseDatas.size());
                        //显示加载课程信息进度条
                        handler.obtainMessage(LOADING).sendToTarget();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    //关闭加载信息的进度条
                                    handler.obtainMessage(FINISH).sendToTarget();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                }
                et_data.setText("");
            }

        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //他弹出窗体的布局控制
                int[] location = new int[2];
                bt_search.getLocationInWindow(location);
                int width = bt_search.getWidth();
                int height = bt_search.getHeight();
                showPopupWindow(view, location[0], location[1] + height);
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, null);
        initView(); //初始化视图
        initData(); //初始化数据
        initEvent(); //初始化事件
        return view;
    }

    /**
     * 初始化弹出窗体
     */
    private void initPopupWindow() {
        popupView = View.inflate(getActivity(), R.layout.popup_course, null);
        Button bt_credit = popupView.findViewById(R.id.bt_popup_credit);
        Button bt_category = popupView.findViewById(R.id.bt_popup_category);
        Button bt_selected = popupView.findViewById(R.id.bt_popup_selected);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_popup_credit: //点击课程学分操作
                        if (pw != null && pw.isShowing()) {
                            bt_search.setText("课程学分");
                            et_data.setHint("请输入学分分值");
                            et_search_data = SEARCH1;
                            pw.dismiss();
                        }

                        break;
                    case R.id.bt_popup_category: //点击课程性质操作操作
                        if (pw != null && pw.isShowing()) {
                            bt_search.setText("课程性质");
                            et_data.setHint("请输入课程性质");
                            et_search_data = SEARCH2;
                            pw.dismiss();
                        }
                        break;
                    case R.id.bt_popup_selected:
                        if (userDao.getIsSelected().equals("n")) {
                            Toast.makeText(getActivity(), "您还没有进行选课!", Toast.LENGTH_SHORT).show();

                        } else {
                            courseDatas.clear();
                            //从数据库中获取该用户所选课程

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    handler.obtainMessage(LOADING).sendToTarget();
                                    courseDatas = dao.getCourseByUserSelectedCourses(userDao.getSelectedCourses());
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    handler.obtainMessage(FINISH).sendToTarget();
                                }
                            }).start();


                        }
                        pw.dismiss();
                        break;
                    default:
                        break;
                }
            }


        };

        bt_credit.setOnClickListener(listener);
        bt_category.setOnClickListener(listener);
        bt_selected.setOnClickListener(listener);
        pw = new PopupWindow(popupView, -2, -2);

        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sa = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0f);

        sa.setDuration(200);


    }

    /**
     * 显示弹出窗体
     */
    public void showPopupWindow(View parent, int x, int y) {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
        } else {
            popupView.startAnimation(sa);
            pw.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, x, y);
        }
    }

    /**
     * 关闭弹出窗体
     */
    public void closePopupWindow() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        if (pw != null && pw.isShowing()) {
            pw.dismiss();
            pw = null;
        }
        super.onDestroy();
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
                convertView = View.inflate(getActivity(), R.layout.item_lv_data, null);
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
