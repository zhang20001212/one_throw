package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.AllInfoNewBean;
import com.onetoall.yjt.domain.PersonalInfomationBean;
import com.onetoall.yjt.domain.Profile;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.domain.User;

/**
 * Created by qinwei on 2016/10/21 16:08
 * email:qinwei_it@163.com
 */

public interface IUserModel {
    /**
     * 登录
     *
     * @param username
     * @param password
     * @param callback
     */
    void login(String username, String password, Callback<Profile> callback);

    /**
     * 加载用户信息
     *
     * @param tel
     * @param callback
     */
    void loadUserInfo(String tel, Callback<User> callback);
    /**
     * 修改密码
     */
    void changePwd(String oldPwd,String newPwd,Callback<Result> callback);
    /**
     * 查询个人信息
     */
    void selectStoreInfo(String storeID,Callback<PersonalInfomationBean> callback);
    /**
     * 修改个人信息
     */
    void changePersonInfo(String tel,String name,String nickName,String sex,Callback<Result> callback);
    /**
     * 找回密码
     */
    void findPwd(String tel,String code,String pwd,Callback<Result> callback);
    /**
     * 发送短信
     */
    void sendVerfiMsg(String tel,Callback<Result> callback);
    /**
     * 实时查询登录用户所有信息
     */
    void querryAllInfoNew(String tel,Callback<AllInfoNewBean> callback);
}
