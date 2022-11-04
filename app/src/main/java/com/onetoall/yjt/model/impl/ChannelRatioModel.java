package com.onetoall.yjt.model.impl;

import android.util.Log;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.ChannelRatio;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IChannelRatioModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by shaomengjie on 2016/11/18.
 */

public class ChannelRatioModel extends BaseModel implements IChannelRatioModel{
    public ChannelRatioModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void loadChanneRatio(String store_id, final Callback<ChannelRatio> callback) {
        HttpRequest request = new HttpRequest(API.getChannelRatio(),HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("id", store_id);
        request.setCallback(new EcpayCallback<ChannelRatio>() {

            @Override
            public void onSuccess(ChannelRatio channelRatio) {
                callback.onSuccess(channelRatio);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadAccountBookIndex",request);
    }
}
