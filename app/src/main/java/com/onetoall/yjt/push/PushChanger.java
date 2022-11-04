package com.onetoall.yjt.push;

import com.qw.framework.utils.Trace;

import java.util.Observable;

/**
 * Created by qinwei on 2016/11/15 10:50
 * email:qinwei_it@163.com
 */

public class PushChanger extends Observable {
    private static PushChanger mInstance;

    private PushChanger() {
    }

    public static PushChanger getInstance() {
        if (mInstance == null) {
            mInstance = new PushChanger();
        }
        return mInstance;
    }

    public void handlerPush(String message) {
        Trace.d(getClass().getSimpleName(), message + "");
        setChanged();
        notifyObservers(message);
    }
}
