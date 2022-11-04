package com.onetoall.yjt.model;

import com.qw.http.AppException;
import com.qw.http.OnGlobalExceptionListener;

/**
 * Created by qinwei on 2016/10/21 16:39
 * email:qinwei_it@163.com
 */

public class BaseModel implements OnGlobalExceptionListener {
    private OnBaseModelListener listener;

    public BaseModel(OnBaseModelListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean parserAppException(AppException e) {
        return listener.onGlobalExceptionHandler(e);
    }
}
