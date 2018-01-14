package com.sinjon.studentsystem.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinjon.studentsystem.R;

/**
 * 课程界面碎片之一 课程信息
 *
 * @作者 xinrong
 * @创建日期 2018/1/9 16:59
 */
public class CourseDataFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, null);
        return view;
    }
}
