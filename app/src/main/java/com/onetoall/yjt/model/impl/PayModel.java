package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.PayOrder;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.domain.PayQRCode;
import com.onetoall.yjt.domain.PayRequestParameter;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IPayModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.Ecpay2001Callback;
import com.onetoall.yjt.net.EcpayCallback;
import com.onetoall.yjt.utils.Constants;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by qinwei on 2016/11/14 10:25
 * email:qinwei_it@163.com
 */

public class PayModel extends BaseModel implements IPayModel {
    public PayModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void collectMoneyByScanner(PayRequestParameter parameter, final Callback<PayOrderDetail> callback) {
        HttpRequest request = new HttpRequest(API.loadAlipayQRCodePay(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.putEncoded("total_fee", parameter.price + "");
        request.putEncoded("auth_code", parameter.qrCode + "");
//        FIXME 这段逻辑有问题  支付宝码目前确实是2开头 不知以后会改不  微信码1开头
        if (parameter.qrCode.indexOf("2") == 0) {
            parameter.type = PayOrder.ALIPAY + "";
        } else if (parameter.qrCode.indexOf("1") == 0) {
            parameter.type = PayOrder.WEIXIN + "";
        }
        request.putEncoded("payment_type", parameter.type + "");//1支付宝 ，2,是微信
        request.putEncoded("cashier", parameter.username);
        request.putEncoded("trans_account", parameter.trans_account + "");
        request.putEncoded("cashier_id", parameter.mobile);
        request.putEncoded("store_id", parameter.store_id);
        String goodsList = "[{\"name\":\"未命名商品\",\"number\":\"1\",\"total\":\"0.01\"}]";
        request.putEncoded("goodsList", goodsList);//该字段暂未用到
        request.putEncoded("merchant_id", parameter.merchant_id);
        request.putEncoded("phone_type", Constants.CLIENT_ANDROID + "");
        request.setCallback(new Ecpay2001Callback<PayOrderDetail>() {
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

//        String token = SputEncodedils.getString(getApplicationContext(), Global.token, "");
//        result = HttpsUtils.postAsynWithToeknPay("api/xlPay/xlPayBarCode", request, token);
    }

    @Override
    public void collectMoneyByAlipayQrCode(PayRequestParameter parameter, final Callback<PayQRCode> callback) {
        HttpRequest request = new HttpRequest(API.loadAlipayQRCode(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.putEncoded("total_fee", parameter.price + "");
        request.putEncoded("auth_code", parameter.qrCode + "");
        request.putEncoded("payment_type", parameter.type);
        request.putEncoded("cashier", parameter.username);
        request.putEncoded("trans_account", parameter.trans_account + "");
        request.putEncoded("cashier_id", parameter.mobile);
        request.putEncoded("store_id", parameter.store_id);
        String goodsList = "[{\"name\":\"未命名商品\",\"number\":\"1\",\"total\":\"0.01\"}]";
        request.putEncoded("goodsList", goodsList);//该字段暂未用到
        request.putEncoded("merchant_id", parameter.merchant_id);
        request.putEncoded("phone_type", Constants.CLIENT_ANDROID + "");
        request.setCallback(new EcpayCallback<PayQRCode>() {
            @Override
            public void onSuccess(PayQRCode s) {
                callback.onSuccess(s);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("collectMoneyByAlipayQrCode", request);
    }

    @Override
    public void collectMoneyByWeixinQrCode(PayRequestParameter parameter, final Callback<PayQRCode> callback) {
        HttpRequest request = new HttpRequest(API.loadWeixinQRCode(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.putEncoded("total_fee", parameter.price + "");
        request.putEncoded("auth_code", parameter.qrCode + "");
        request.putEncoded("payment_type", parameter.type);
        request.putEncoded("cashier", parameter.username);
        request.putEncoded("trans_account", parameter.trans_account + "");
        request.putEncoded("cashier_id", parameter.mobile);
        request.putEncoded("store_id", parameter.store_id);
        String goodsList = "[{\"name\":\"未命名商品\",\"number\":\"1\",\"total\":\"0.01\"}]";
        request.putEncoded("goodsList", goodsList);//该字段暂未用到
        request.putEncoded("merchant_id", parameter.merchant_id);
        request.putEncoded("phone_type", Constants.CLIENT_ANDROID + "");
        request.setCallback(new EcpayCallback<PayQRCode>() {
            @Override
            public void onSuccess(PayQRCode s) {
                callback.onSuccess(s);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("collectMoneyByAlipayQrCode", request);
    }
}
