package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * Created by qinwei on 2016/10/21 17:52
 * email:qinwei_it@163.com
 */

public class User implements Serializable {
    private String user_name;

    private String gender;

    private int user_id;

    private String mobile;

    private int crdate;

    private String email;

    private String nickname;

    private String user_lev;

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setCrdate(int crdate) {
        this.crdate = crdate;
    }

    public int getCrdate() {
        return this.crdate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setUser_lev(String user_lev) {
        this.user_lev = user_lev;
    }

    public String getUser_lev() {
        return this.user_lev;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_name='" + user_name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public String getJobName() {
        if (getUser_lev() != null) {
            switch (getUser_lev()) {
                case "1":
                    return "职位: 店长";
                case "0":
                    return "职位: 店员";
            }
        }
        return "";
    }

    /**
     * 是否是店长
     *
     * @return
     */
    public boolean isShopkeeper() {
        if (user_lev.equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
