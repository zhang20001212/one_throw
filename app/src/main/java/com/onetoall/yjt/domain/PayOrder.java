package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * Created by qinwei on 2016/10/22 18:37
 * email:qinwei_it@163.com
 */

public class PayOrder implements Serializable {
    public static final int ALIPAY = 1;//支付宝
    public static final int WEIXIN = 2;//微信
    public static final int MONEY = 4;
    private int payment_type;

    private long data_long;

    private String create_time;

    private String cashier;

    private int id;

    private String good_amount;

    private int date_int;

    private int status;

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public int getPayment_type() {
        return this.payment_type;
    }

    public void setData_long(long data_long) {
        this.data_long = data_long;
    }

    public long getData_long() {
        return this.data_long;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_time() {
        return this.create_time;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getCashier() {
        return this.cashier;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setGood_amount(String good_amount) {
        this.good_amount = good_amount;
    }

    public String getGood_amount() {
        return this.good_amount;
    }

    public void setDate_int(int date_int) {
        this.date_int = date_int;
    }

    public int getDate_int() {
        return this.date_int;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }
}
