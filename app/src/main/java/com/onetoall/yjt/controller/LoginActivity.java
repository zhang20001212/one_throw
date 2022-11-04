package com.onetoall.yjt.controller;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.config.EnvironmentConfigActivity;
import com.onetoall.yjt.controller.profile.PersonPwdFindActivity;
import com.onetoall.yjt.controller.store.ChoseStoreActivity;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.Profile;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.UserModel;
import com.onetoall.yjt.push.PushManager;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.qw.framework.utils.TextUtil;
import com.qw.framework.utils.Trace;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private UserModel userModel;
    private EditText mLoginAccountEdt;
    private EditText mLoginPasswordEdt;
    private Button mLoginCommitBtn;
    private TextView mForgetPwdLabel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        translucentStatus();
        mLoginAccountEdt = (EditText) findViewById(R.id.mLoginAccountEdt);
        mLoginPasswordEdt = (EditText) findViewById(R.id.mLoginPasswordEdt);
        mLoginCommitBtn = (Button) findViewById(R.id.mLoginCommitBtn);
        mForgetPwdLabel =  (TextView) findViewById(R.id.mForgetPwdLabel);
        mForgetPwdLabel.setOnClickListener(this);
        mLoginCommitBtn.setOnClickListener(this);
        findViewById(R.id.mEnvironmentSettingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, EnvironmentConfigActivity.class));
            }
        });
        if(!MyApplication.getInstance().isDev()){
            findViewById(R.id.mEnvironmentSettingsBtn).setVisibility(View.GONE);
        }
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_login));
        String account = PrefsAccessor.getInstance(getApplicationContext()).getString(Constants.PFA_ACCOUNT);
        String password = PrefsAccessor.getInstance(getApplicationContext()).getString(Constants.PFA_PWD);
        mLoginAccountEdt.setText(account);
        mLoginPasswordEdt.setText(password);
        userModel = new UserModel(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mLoginCommitBtn:
                String account = mLoginAccountEdt.getText().toString();
                String password = mLoginPasswordEdt.getText().toString();
                if (isValidate(account, password)) {
                    doLogin(account, password);
                } else {
                    Toast.makeText(this, R.string.mLoginInputEmptyMsg, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mForgetPwdLabel:
                UMEventUtil.onEvent(this, UMEvent.forget);
                startActivity(new Intent(LoginActivity.this, PersonPwdFindActivity.class).putExtra(Constants.PWD_FIND_USERNAME,mLoginAccountEdt.getText().toString().trim()));
                break;
            default:
                break;
        }
    }

    private void doLogin(final String account, final String password) {
        UMEventUtil.onEvent(this, UMEvent.login);
        showProgress(getString(R.string.mLoginLabel));
        PrefsAccessor.getInstance(getApplicationContext()).saveString(Constants.PFA_TOKEN,null);//登陆前清空token
        userModel.login(account, password, new Callback<Profile>() {
            @Override
            public void onSuccess(Profile data) {
                closeProgress();
                Trace.d(data.getUser_info().toString());
                PrefsAccessor.getInstance(getApplicationContext()).saveString(Constants.PFA_ACCOUNT, account);
                PrefsAccessor.getInstance(getApplicationContext()).saveString(Constants.PFA_PWD, password);
                MyApplication.getInstance().setToken(data.getToken());
                MyApplication.getInstance().setUser(data.getUser_info());
                MyApplication.getInstance().setStoreNumber(data.getStore_arr().size());
                setPushAlias();
                if (data.getStore_arr().size() == 1) {
                    MyApplication.getInstance().setStore(data.getStore_arr().get(0));
                    goHome();
                } else if(data.getStore_arr().size() > 1){
                    MyApplication.getInstance().setStore(data.getStore_arr().get(0));
                   goChoseStore();
                }else {
                    Toast.makeText(getApplicationContext(), R.string.mLoginUserPermissionLowMsg, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int code, String msg) {
//                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                Trace.e(msg);
            }
        });
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void setPushAlias() {
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        PushManager.getInstance().setPushAlias(this,androidID);
    }
    private void goChoseStore() {
        Intent intent = new Intent(this, ChoseStoreActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isValidate(String account, String password) {
//        TODO check input content
        return TextUtil.isValidate(account, password);
    }

    @Override
    protected boolean hasLeftIcon() {
        return false;
    }
}
