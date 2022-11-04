package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * Created by qinwei on 2016/11/7 14:10
 * email:qinwei_it@163.com
 */

public class QRCodeInfo implements Serializable {
    private String store_id;

    private String qrcode_id;

    private String update_time;

    private String binding_time;

    private String create_time;

    private String batch;

    private int is_binding;

    private int id;

    private String url;

    private String content;

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_id() {
        return this.store_id;
    }

    public void setQrcode_id(String qrcode_id) {
        this.qrcode_id = qrcode_id;
    }

    public String getQrcode_id() {
        return this.qrcode_id;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_time() {
        return this.update_time;
    }

    public void setBinding_time(String binding_time) {
        this.binding_time = binding_time;
    }

    public String getBinding_time() {
        return this.binding_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_time() {
        return this.create_time;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBatch() {
        return this.batch;
    }

    public void setIs_binding(int is_binding) {
        this.is_binding = is_binding;
    }

    public int getIs_binding() {
        return this.is_binding;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
