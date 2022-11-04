package com.onetoall.yjt.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by qinwei on 2016/11/28 9:34
 * email:qinwei_it@163.com
 */

public class UMEventUtil {
    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    public static void onEvent(Context context, String eventId, HashMap<String, String> map) {
        MobclickAgent.onEvent(context, eventId, map);
    }
}
