package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.AccountBookIndex;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IAccountBookModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by qinwei on 2016/10/22 18:48
 * email:qinwei_it@163.com
 */

public class AccountBookModel extends BaseModel implements IAccountBookModel {
    public AccountBookModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void loadAccountBookIndex(String store_id, String dataLong, final Callback<AccountBookIndex> callback) {
        HttpRequest request = new HttpRequest(API.loadAccountBookIndex(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("store_id", store_id);
        request.put("dataLong", dataLong);
        request.setCallback(new EcpayCallback<AccountBookIndex>() {
            @Override
            public void onSuccess(AccountBookIndex data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadAccountBookIndex", request);
    }

    @Override
    public void loadPayOrderById(String orderId, final Callback<PayOrderDetail> callback) {
        HttpRequest request = new HttpRequest(API.loadPayOrderById(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("id", orderId);
        request.setCallback(new EcpayCallback<PayOrderDetail>() {
            @Override
            public void onSuccess(PayOrderDetail data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadPayOrderById", request);
    }
}
