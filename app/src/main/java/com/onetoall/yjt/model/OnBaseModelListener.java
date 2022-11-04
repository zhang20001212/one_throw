package com.onetoall.yjt.model;

import com.qw.http.AppException;

/**
 * Created by qinwei on 2016/10/21 17:17
 * email:qinwei_it@163.com
 */
public interface OnBaseModelListener {
    boolean onGlobalExceptionHandler(AppException e);
}
