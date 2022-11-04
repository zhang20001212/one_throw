package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * Created by qinwei on 2016/10/22 16:57
 * email:qinwei_it@163.com
 */

public class Store implements Serializable {
    private long  store_id;
    private String area;

    private String contacts_phone;

    private String address;

    private String mailbox;


    private String merchant_name;


    private String business_area;

    private String merchant_number;

    private String contacts;

    private String store_number;

    private String store_name;

    private String client;

    private long merchant_id;

    private String lev;


    public long getStore_id() {
        return this.store_id;
    }

    public void setStore_id(long store_id) {
        this.store_id = store_id;
    }

    public void setStore_number(String store_number) {
        this.store_number = store_number;
    }

    public String getStore_number() {
        return this.store_number;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_name() {
        return this.store_name;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClient() {
        return this.client;
    }

    public void setMerchant_id(long merchant_id) {
        this.merchant_id = merchant_id;
    }

    public long getMerchant_id() {
        return this.merchant_id;
    }

    public void setLev(String lev) {
        this.lev = lev;
    }

    public String getLev() {
        return this.lev;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContacts_phone() {
        return contacts_phone;
    }

    public void setContacts_phone(String contacts_phone) {
        this.contacts_phone = contacts_phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getBusiness_area() {
        return business_area;
    }

    public void setBusiness_area(String business_area) {
        this.business_area = business_area;
    }

    public String getMerchant_number() {
        return merchant_number;
    }

    public void setMerchant_number(String merchant_number) {
        this.merchant_number = merchant_number;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getJobName() {
        switch (lev) {
            case "1":
                return "职位: 店长";
            case "0":
                return "职位: 店员";
        }
        return "";
    }

    public boolean isShopkeeper() {
        if (lev.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
