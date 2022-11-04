package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.domain.PayQRCode;
import com.onetoall.yjt.domain.PayRequestParameter;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IPayModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.onetoall.yjt.utils.Constants;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by user on 2016/11/14.
 */

public class PayCashModel extends BaseModel implements IPayModel {
    public PayCashModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void collectMoneyByScanner(PayRequestParameter parameter, final Callback<PayOrderDetail> callback) {
        HttpRequest request = new HttpRequest(API.putCashPay(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.putEncoded("total_fee", parameter.price + "");
            request.putEncoded("payment_type", "4");
        request.putEncoded("cashier", parameter.username);
        request.putEncoded("trans_account", parameter.trans_account + "");
        request.putEncoded("cashier_id", parameter.mobile);
        request.putEncoded("store_id", parameter.store_id);
        String goodsList = "[{\"name\":\"未命名商品\",\"number\":\"1\",\"total\":\"0.01\"}]";
        request.putEncoded("goodsList", goodsList);//该字段暂未用到
        request.putEncoded("merchant_id", parameter.merchant_id);
        request.putEncoded("phone_type", Constants.CLIENT_ANDROID + "");
        request.setCallback(new EcpayCallback<PayOrderDetail>() {
            @Override
            public void onSuccess(PayOrderDetail s) {
                callback.onSuccess(s);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("collectMoneyByScanner", request);
    }

    @Override
    public void collectMoneyByAlipayQrCode(PayRequestParameter parameter, Callback<PayQRCode> callback) {

    }

    @Override
    public void collectMoneyByWeixinQrCode(PayRequestParameter parameter, Callback<PayQRCode> callback) {

    }
}
