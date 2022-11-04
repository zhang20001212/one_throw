package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.domain.PayQRCode;
import com.onetoall.yjt.domain.PayRequestParameter;

/**
 * Created by qinwei on 2016/11/14 9:57
 * email:qinwei_it@163.com
 */

public interface IPayModel {
    //        httpArgs.put("total_fee", totle_price + "");
//        httpArgs.put("auth_code", RQnum + "");
//        if (RQnum.substring(0, 1).equals("2")) {
//            httpArgs.put("payment_type", "1");
//        } else {
//            httpArgs.put("payment_type", "2");
//        }
//
//        httpArgs.put("cashier", SPUtils.getString(getApplicationContext(), Global.username, ""));
//        httpArgs.put("trans_account", trans_account + "");
//        httpArgs.put("cashier_id", SPUtils.getString(getApplicationContext(), Global.mobile, ""));
//        httpArgs.put("store_id", SPUtils.getString(getApplicationContext(), Global.storeId, ""));
//        httpArgs.put("goodsList", goodsList);
//        httpArgs.put("merchant_id", SPUtils.getString(getApplicationContext(), Global.merchant_id, ""));
//        httpArgs.put("phone_type", Global.XITONG + "");
//        String token = SPUtils.getString(getApplicationContext(), Global.token, "");
//        result = HttpsUtils.postAsynWithToeknPay("api/xlPay/xlPayBarCode", httpArgs, token);
    void collectMoneyByScanner(PayRequestParameter parameter, Callback<PayOrderDetail> callback);

    void collectMoneyByAlipayQrCode(PayRequestParameter parameter, Callback<PayQRCode> callback);
    void collectMoneyByWeixinQrCode(PayRequestParameter parameter, Callback<PayQRCode> callback);
}
