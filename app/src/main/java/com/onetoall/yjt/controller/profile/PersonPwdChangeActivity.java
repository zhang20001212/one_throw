package com.onetoall.yjt.controller.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.Profile;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.UserModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.qw.framework.utils.Trace;

/**
 * 修改密码
 * Created by user on 2016/11/7.
 */

public class PersonPwdChangeActivity extends BaseActivity{
    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mNewPassword2;
    private UserModel mUserMolde;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_person_pwd,true);
    }

    @Override
    protected void initView() {
        assignViews();
        setTitle("修改密码");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mUserMolde = new UserModel(this);
    }
    private void assignViews() {
        mOldPassword = (EditText) findViewById(R.id.old_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mNewPassword2 = (EditText) findViewById(R.id.new_password_2);
    }
    public void click(View view){
        UMEventUtil.onEvent(this,UMEvent.setpersonchangepasssave);
        if (verfiPwd()) return;
        showProgress();
        mUserMolde.changePwd(mOldPassword.getText().toString().trim(), mNewPassword.getText().toString().trim(), new Callback<Result>() {
            @Override
            public void onSuccess(Result data) {
                Trace.d(data.toString());

                mUserMolde.login(MyApplication.getInstance().getUser().getUser_name(), mNewPassword.getText().toString(), new Callback<Profile>() {
                    @Override
                    public void onSuccess(Profile data) {
                        closeProgress();
                        showToast("密码修改成功!!");
                        PrefsAccessor.getInstance(getApplicationContext()).saveString(Constants.PFA_PWD, mNewPassword.getText().toString());
                        MyApplication.getInstance().setToken(data.getToken());
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {

                    }
                });


            }

            @Override
            public void onFailure(int code, String msg) {
                Trace.d("fail-->"+msg);
            }
        });
    }

    private boolean verfiPwd() {
        if (TextUtils.isEmpty(mOldPassword.getText().toString().trim())){
            showToast("请输入原密码");
            return true;
        }
        if (mOldPassword.getText().toString().trim().length()<6){
            showToast("原密码长度低于6位");
            return true;
        }
        if (TextUtils.isEmpty(mNewPassword.getText().toString().trim())){
            showToast("请输入新密码");
            return true;
        }
        if (mNewPassword.getText().toString().trim().length()<6){
            showToast("新密码长度低于6位");
            return true;
        }
        if (!mNewPassword.getText().toString().trim().equals(mNewPassword2.getText().toString().trim())){
            showToast("两次输入密码不一致");
            return true;
        }
        if (mOldPassword.getText().toString().trim().equals(mNewPassword.getText().toString().trim())){
            showToast("新密码与原密码相同，请重新输入");
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this,UMEvent.setpersonchangepassback);
    }
}
