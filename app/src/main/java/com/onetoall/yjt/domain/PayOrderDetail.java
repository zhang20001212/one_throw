package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * Created by qinwei on 2016/10/27 21:13
 * email:qinwei_it@163.com
 */

public class PayOrderDetail implements Serializable{
    private double count_fee;
    private long store_id;
    private String cashier_id;
    private long data_long;
    private int phone_type;
    private long create_time;
    private String trans_account;
    private String trans_order;
    private double discount;
    private double count_fee_money;
    private String trans_time;
    private String buyer_id;
    private String buyer;
    private long update_time;
    private String payment_type;
    private double total_amount;
    private String pay_order;
    private String cashier;
    private double settlement_amount;
    private int id;
    private double good_amount;
    private long date_int;
    private int status;
    private int payment_code;


    public double getCount_fee() {
        return count_fee;
    }

    public void setCount_fee(double count_fee) {
        this.count_fee = count_fee;
    }

    public long getStore_id() {
        return store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public String getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(String cashier_id) {
        this.cashier_id = cashier_id;
    }

    public long getData_long() {
        return data_long;
    }

    public void setData_long(long data_long) {
        this.data_long = data_long;
    }

    public int getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(int phone_type) {
        this.phone_type = phone_type;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getTrans_account() {
        return trans_account;
    }

    public void setTrans_account(String trans_account) {
        this.trans_account = trans_account;
    }

    public String getTrans_order() {
        return trans_order;
    }

    public void setTrans_order(String trans_order) {
        this.trans_order = trans_order;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getCount_fee_money() {
        return count_fee_money;
    }

    public void setCount_fee_money(double count_fee_money) {
        this.count_fee_money = count_fee_money;
    }

    public String getTrans_time() {
        return trans_time;
    }

    public void setTrans_time(String trans_time) {
        this.trans_time = trans_time;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getPay_order() {
        return pay_order;
    }

    public void setPay_order(String pay_order) {
        this.pay_order = pay_order;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public double getSettlement_amount() {
        return settlement_amount;
    }

    public void setSettlement_amount(double settlement_amount) {
        this.settlement_amount = settlement_amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGood_amount() {
        return good_amount;
    }

    public void setGood_amount(double good_amount) {
        this.good_amount = good_amount;
    }

    public long getDate_int() {
        return date_int;
    }

    public void setDate_int(int date_int) {
        this.date_int = date_int;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(int payment_code) {
        this.payment_code = payment_code;
    }

    public String getPayUserName() {
        if (payment_type.equals("1")) {
            return "支付宝";
        } else if (payment_type.equals("2")) {
            return "微信";
        } else if (payment_type.equals("4")) {
            return "现金";
        }
        return "其他";
    }
}
