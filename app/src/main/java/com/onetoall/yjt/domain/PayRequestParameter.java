package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * Created by qinwei on 2016/11/14 10:33
 * email:qinwei_it@163.com
 */

public class PayRequestParameter implements Serializable {
    public static final String TRANS_ACCOUNT_TYPE_ALIPAY = "支付宝账户";
    public static final String TRANS_ACCOUNT_TYPE_WEIXIN = "微信账户";
    public static final String TRANS_ACCOUNT_TYPE_MONEY = "现金账户";
    public static final String PAY_TYPE_ALIPAY = "1";//1支付宝
    public static final String PAY_TYPE_WEIXIN = "2";//2微信
    public static final String PAY_TYPE_MONEY = "3";// 3 一码付
    public static final String PAY_TYPE_QRCODE = "4";// 4 现金
    public String price;//收款金额
    public String qrCode;//消费者付款码信息
    public String type;//支付类型 1支付宝  2微信  3 一码付  4 现金
    public String store_id;//门店id
    public String username;//当前用户账号
    public String trans_account;//支付宝账户，微信账户，现金账户
    public String mobile;//手机号 user.Mobile
    public String merchant_id;//store.merchant_id
}
