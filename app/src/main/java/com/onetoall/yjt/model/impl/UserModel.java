package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.AllInfoNewBean;
import com.onetoall.yjt.domain.PersonalInfomationBean;
import com.onetoall.yjt.domain.Profile;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.domain.User;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IUserModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.onetoall.yjt.net.EcpayResultCallBack;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by qinwei on 2016/10/21 16:06
 * email:qinwei_it@163.com
 */

public class UserModel extends BaseModel implements IUserModel{
    public UserModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void login(String username, String password, final Callback<Profile> callback) {
        HttpRequest request = new HttpRequest(API.loadLogin(), HttpRequest.RequestMethod.POST);
        request.put("username", username);
        request.put("password", password);
        request.setCallback(new EcpayCallback<Profile>() {
            @Override
            public void onSuccess(Profile s) {
                callback.onSuccess(s);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("login", request);
    }

    @Override
    public void loadUserInfo(String tel, final Callback<User> callback) {
        HttpRequest request = new HttpRequest(API.loadUserInfo(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("tel", tel);
        request.setCallback(new EcpayCallback<User>() {
            @Override
            public void onSuccess(User s) {
                callback.onSuccess(s);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadUserInfo", request);
    }

    @Override
    public void changePwd(String oldPwd, String newPwd,final Callback<Result> callback) {
        HttpRequest request = new HttpRequest(API.changePwd(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("tel", MyApplication.getInstance().getTel());
        request.put("old_password",oldPwd);
        request.put("new_password", newPwd);
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result s) {
                callback.onSuccess(s);

            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("changePwd", request);
    }

    @Override
    public void selectStoreInfo(String storeID, final Callback<PersonalInfomationBean> callback) {
        HttpRequest request = new HttpRequest(API.selectMerstoreInfo(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("store_id", MyApplication.getInstance().getStore().getStore_id()+"");
        request.setCallback(new EcpayCallback<PersonalInfomationBean>() {
            @Override
            public void onSuccess(PersonalInfomationBean s) {
                callback.onSuccess(s);

            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("selectStoreInfo", request);
    }

    @Override
    public void changePersonInfo(String tel,String name ,String nickName ,String sex,final Callback<Result> callback) {
        HttpRequest request = new HttpRequest(API.changeUserInfo(), HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("tel", tel);
        request.put("username",name);
        request.put("nikename", nickName);
        request.put("sex", sex);
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result s) {
                callback.onSuccess(s);

            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("changePersonInfo", request);
    }

    @Override
    public void findPwd(String tel, String code,String pwd,final Callback<Result> callback) {
        HttpRequest request = new HttpRequest(API.resetPwd(), HttpRequest.RequestMethod.POST);
        request.put("tel", tel);
        request.put("code",code);
        request.put("password", pwd);
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result s) {
                callback.onSuccess(s);

            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("findPwd", request);
    }
    @Override
    public void sendVerfiMsg(String tel,final Callback<Result> callback) {
        HttpRequest request = new HttpRequest(API.sendVerfiMsg(), HttpRequest.RequestMethod.POST);
        request.put("tel",tel);
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result s) {
                callback.onSuccess(s);

            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("sendVerfiMsg", request);
    }

    @Override
    public void querryAllInfoNew(String tel, final Callback<AllInfoNewBean> callback) {
        HttpRequest request = new HttpRequest(API.querryAllInfoNew(), HttpRequest.RequestMethod.POST);
        request.put("tel",tel);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.setCallback(new EcpayCallback<AllInfoNewBean>() {
            @Override
            public void onSuccess(AllInfoNewBean s) {
                callback.onSuccess(s);

            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("querryAllInfoNew", request);
    }


}
