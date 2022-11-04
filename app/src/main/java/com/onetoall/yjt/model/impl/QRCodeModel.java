package com.onetoall.yjt.model.impl;


import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.QRCodeInfo;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IQRCodeModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.onetoall.yjt.net.EcpayResultCallBack;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

import java.util.ArrayList;

/**
 * Created by qinwei on 2016/11/7 14:25
 * email:qinwei_it@163.com
 */

public class QRCodeModel extends BaseModel implements IQRCodeModel {
    public static final String LOAD_QR_CODES = "loadQRCodes";

    public QRCodeModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void loadQRCodes(String storeId, final Callback<ArrayList<QRCodeInfo>> callback) {
        HttpRequest request = new HttpRequest(API.loadQRCodes(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("store_id", storeId);
        request.setCallback(new EcpayCallback<ArrayList<QRCodeInfo>>() {
            @Override
            public void onSuccess(ArrayList<QRCodeInfo> o) {
                callback.onSuccess(o);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute(LOAD_QR_CODES, request);
    }

    @Override
    public void bindQrCode(String store_id, String qrcode_id, final Callback<Result> callback) {
        HttpRequest request = new HttpRequest(API.loadBindQrCode(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("store_id", store_id);
        request.put("qrcode_id", qrcode_id);
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result o) {
                callback.onSuccess(o);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("bindQrCode", request);
    }
}
