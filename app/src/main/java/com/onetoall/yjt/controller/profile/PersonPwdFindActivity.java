package com.onetoall.yjt.controller.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.UserModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.time.TimeButton;

import static com.onetoall.yjt.R.id.phone;
import static com.onetoall.yjt.R.id.vertify;

/**
 * Created by user on 2016/11/17.
 */

public class PersonPwdFindActivity extends BaseActivity {
    private EditText mPhoneInput;
    private EditText mVertifyCodeInput;
    private TimeButton mVertifyGetBtn;
    private EditText mPasswordInput;
    private EditText mPassword2Input;
    private Button mSureBtn;
    private UserModel mUserModel;

    private void assignViews() {
        mPhoneInput = (EditText) findViewById(phone);
        mVertifyCodeInput = (EditText) findViewById(vertify);
        mVertifyGetBtn = (TimeButton) findViewById(R.id.vertify_get);
        mPasswordInput = (EditText) findViewById(R.id.password);
        mPassword2Input = (EditText) findViewById(R.id.password_2);
        mSureBtn = (Button) findViewById(R.id.sure_btn);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pwdfind, true);
    }

    @Override
    protected void initView() {
        setTitle("找回密码");
        assignViews();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPhoneInput.setText(getIntent().getStringExtra(Constants.PWD_FIND_USERNAME));
        mUserModel = new UserModel(this);
        initLinstener();
    }

    private void initLinstener() {
        mVertifyGetBtn.setOnTimeChangedListener(new TimeButton.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(int ss) {
                mVertifyGetBtn.setText(ss + "秒后重新获取");
                mVertifyGetBtn.setEnabled(false);
            }

            @Override
            public void onTimeCompleted() {
                mVertifyGetBtn.reset();
                mVertifyGetBtn.setText("点击获取验证码");
            }
        });
        mSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verfiPhoneIs()) {
                    return;
                }
                if (verfiPwdIs()) {
                    return;
                }
                findPwd(mPhoneInput.getText().toString().trim(), mVertifyCodeInput.getText().toString().trim(), mPassword2Input.getText().toString().toString());
            }
        });
        mVertifyGetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verfiPhoneIs()) return;
                sendVerfiMsg(mPhoneInput.getText().toString().trim());
            }
        });
    }

    private boolean verfiPwdIs() {
        if (TextUtils.isEmpty(mVertifyCodeInput.getText().toString().trim())) {
            showToast("请输入验证码");
            return true;
        }
        if (mVertifyCodeInput.getText().toString().trim().length() < 6) {
            showToast("验证码输入有误");
            return true;
        }
        if (TextUtils.isEmpty(mPasswordInput.getText().toString().trim())) {
            showToast("请输入新密码");
            return true;
        }
        if (mPasswordInput.getText().toString().trim().length() < 6) {
            showToast("密码应不小于6位");
            return true;
        }
        if (!mPasswordInput.getText().toString().trim().equals(mPassword2Input.getText().toString().trim())) {
            showToast("两次输入密码不一致");
            return true;
        }
        return false;
    }


    /**
     * 验证手机号码格式
     *
     * @return
     */
    private boolean verfiPhoneIs() {
        if (TextUtils.isEmpty(mPhoneInput.getText().toString().trim())) {
            showToast("请输入手机号码");
            return true;
        }
        if (mPhoneInput.getText().toString().trim().length() < 11) {
            showToast("请输入正确的手机号码");
            return true;
        }
        return false;
    }

    private void findPwd(String tel, String code, String pwd) {
        UMEventUtil.onEvent(this, UMEvent.forgetok);
        showProgress("");
        mUserModel.findPwd(tel, code, pwd, new Callback<Result>() {
            @Override
            public void onSuccess(Result data) {
                closeProgress();
                showToast("找回密码成功，请登录");
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    private void sendVerfiMsg(String tel) {
        UMEventUtil.onEvent(this, UMEvent.forgetcode);
        mUserModel.sendVerfiMsg(tel, new Callback<Result>() {
            @Override
            public void onSuccess(Result data) {
                showToast("短信发送成功,请注意查收");
                mVertifyGetBtn.start();
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this, UMEvent.forgetback);
    }
}
