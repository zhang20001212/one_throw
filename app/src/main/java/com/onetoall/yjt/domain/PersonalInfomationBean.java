package com.onetoall.yjt.domain;

/**
 * Created by user on 2016/11/15.
 */

public class PersonalInfomationBean {
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(Object merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    private String area;
    private String address;
    private Object merchant_name;
    private String store_name;
}
