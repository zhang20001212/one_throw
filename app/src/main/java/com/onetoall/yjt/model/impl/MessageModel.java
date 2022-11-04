package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.MessageBean;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IMessageModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.onetoall.yjt.net.EcpayListCallback;
import com.onetoall.yjt.net.EcpayResultCallBack;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

import java.util.ArrayList;

/**
 * Created by shaomengjie on 2016/12/6.
 */

public class MessageModel extends BaseModel implements IMessageModel{
    public MessageModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void loadMessage(String id, String username, final Callback<ArrayList<MessageBean>> callback) {
        HttpRequest request = new HttpRequest(API.getMessageList(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("id", id);
        request.put("username",username);
        request.setCallback(new EcpayListCallback<ArrayList<MessageBean>>() {

            @Override
            public void onSuccess(ArrayList<MessageBean> messageBeen) {
                callback.onSuccess(messageBeen);
            }

            @Override
            public void onFailure(AppException e) {

            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadMessageModel",request);
    }

    @Override
    public void commitUpdata(String id) {
        HttpRequest request = new HttpRequest(API.commitUpdata(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("id", id);
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result o) {

            }

            @Override
            public void onFailure(AppException e) {

            }
        });
        request.setOnGlobalExceptionListener(this);

        RequestManager.getInstance().execute("commitUpdata",request);
    }
}
