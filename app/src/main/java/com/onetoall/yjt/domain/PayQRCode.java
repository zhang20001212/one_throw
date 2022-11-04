package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * app付款码实体模型
 * Created by qinwei on 2016/11/14 14:22
 * email:qinwei_it@163.com
 */

public class PayQRCode implements Serializable {
    private String qrcode;//付款码信息

    private String total_fee;//多少钱

    private String pay_num;//数量

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getQrcode() {
        return this.qrcode;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTotal_fee() {
        return this.total_fee;
    }

    public void setPay_num(String pay_num) {
        this.pay_num = pay_num;
    }

    public String getPay_num() {
        return this.pay_num;
    }
}
