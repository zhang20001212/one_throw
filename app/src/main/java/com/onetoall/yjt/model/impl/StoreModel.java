package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.Store;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IStoreModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by qinwei on 2016/10/23 15:46
 * email:qinwei_it@163.com
 */

public class StoreModel extends BaseModel implements IStoreModel {
    public StoreModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void loadStoreInfo(String storeId, String tel, String storeNumber, final Callback<Store> callback) {
        HttpRequest request = new HttpRequest(API.loadStoreInfo(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("store_id", storeId);
        request.put("tel", tel);
        request.put("store_number", storeNumber);
        request.setCallback(new EcpayCallback<Store>() {
            @Override
            public void onSuccess(Store s) {
                callback.onSuccess(s);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadStoreInfo", request);
    }
}
