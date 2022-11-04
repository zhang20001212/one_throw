package com.onetoall.yjt.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qinwei on 2016/10/22 16:55
 * email:qinwei_it@163.com
 */

public class Profile implements Serializable{
    private User user_info;

    private List<Store> store_arr;

    private String token;

    public void setUser(User user_info) {
        this.user_info = user_info;
    }

    public User getUser_info() {
        return this.user_info;
    }

    public void setStore_arr(List<Store> store_arr) {
        this.store_arr = store_arr;
    }

    public List<Store> getStore_arr() {
        return this.store_arr;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
