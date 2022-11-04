package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.CollectionMoneyBean;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.ICollectionMoney;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by shaomengjie on 2016/11/20.
 */

public class CollectionMoneyModel extends BaseModel implements ICollectionMoney{
    public CollectionMoneyModel(OnBaseModelListener listener) {
        super(listener);
    }


    @Override
    public void loadCollectionMoney(String store_id, String rows, final Callback<CollectionMoneyBean> callback) {
        HttpRequest request = new HttpRequest(API.loadCollectionMoney(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("store_id", store_id);
        request.put("rows",rows);
        request.setCallback(new EcpayCallback<CollectionMoneyBean>() {

            @Override
            public void onSuccess(CollectionMoneyBean collectionMoneyBean) {
                callback.onSuccess(collectionMoneyBean);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadCollectionMoney",request);
    }
}
