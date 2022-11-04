package com.onetoall.yjt.model;

/**
 * Created by qinwei on 2016/10/21 17:49
 * email:qinwei_it@163.com
 */

public interface Callback<T> {
    void onSuccess(T data);

    void onFailure(int code, String msg);
}
