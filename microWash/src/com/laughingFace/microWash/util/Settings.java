package com.laughingFace.microWash.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.laughingFace.microWash.MyApplication;

/**
 * Created by zihao on 15-6-9.
 */
public class Settings {
    public static final String SETTING_ISFIRST = "isFirst";

    public static final String TIMING_MODEL_NAME = "null";
    public static final String TIMING_MODEL_BEGIN = "beginTime";
    public static final String TIMING_MODEL_HOW_LONG = "howLong";

    private static String fileName = "microWashSettings";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    static {
        preferences = MyApplication.getContext().getSharedPreferences(fileName, Context.MODE_APPEND);
        editor = preferences.edit();
    }


    public static boolean isFirst() {

        return preferences.getBoolean(SETTING_ISFIRST,true);
    }
    public static void setIsFirst(boolean isFirst) {
        editor.putBoolean(SETTING_ISFIRST, isFirst);
        editor.commit();
    }

    public static String getTimingModelName() {
        return preferences.getString(TIMING_MODEL_NAME,"null");
    }

    public static long getTimingModelBegin() {
        return preferences.getLong(TIMING_MODEL_BEGIN, -1);
    }

    public static  long  getTimingModelHowLong() {
        return preferences.getLong(TIMING_MODEL_HOW_LONG,0);
    }

    public static void setTimingModelName(String name) {
        editor.putString(TIMING_MODEL_NAME,name);
        editor.commit();
    }

    public static void setTimingModelBegin(long beginTime) {
        editor.putLong(TIMING_MODEL_BEGIN,beginTime);
        editor.commit();
    }

    public static void  setTimingModelHowLong(long howLong) {
        editor.putLong(TIMING_MODEL_HOW_LONG,howLong);
        editor.commit();
    }
}
