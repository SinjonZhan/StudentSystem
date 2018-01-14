package com.sinjon.studentsystem.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP工具类
 *
 * @作者 xinrong
 * @创建日期 2018/1/8 23:07
 */
public class SpTools {
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }


    public static String getString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
        return sp.getString(key, value);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }


    public static boolean getBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPFILE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, value);
    }


}
