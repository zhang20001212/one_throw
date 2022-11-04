package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.domain.ApkBean;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IUpdateModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by user on 2016/11/22.
 */

public class UpdateModel extends BaseModel implements IUpdateModel {
    public UpdateModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void loadApkInfo(String versonCode, final Callback<ApkBean> callback) {
        HttpRequest request = new HttpRequest(API.updataApk(), HttpRequest.RequestMethod.POST);
//        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("versionCode", versonCode);
        request.setCallback(new EcpayCallback<ApkBean>() {
            @Override
            public void onSuccess(ApkBean s) {
                callback.onSuccess(s);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
//        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadStoreInfo", request);
    }


}
