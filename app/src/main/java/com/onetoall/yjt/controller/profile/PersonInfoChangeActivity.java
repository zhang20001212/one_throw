package com.onetoall.yjt.controller.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.UserModel;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;

/**
 * 个人信息修改
 * Created by user on 2016/11/7.
 */

public class PersonInfoChangeActivity extends BaseActivity {
    private EditText mNameInput;
    private RadioButton mManBtn;
    private RadioButton mWomanBtn;
    private UserModel userModel;
    private int sex;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personalinfo_change, true);
    }

    @Override
    protected void initView() {
        setTitle("修改信息");
        assignView();
    }

    private void assignView() {
        mManBtn = (RadioButton) findViewById(R.id.man);
        mWomanBtn = (RadioButton) findViewById(R.id.woman);
        mNameInput = (EditText) findViewById(R.id.ed_1);
        mNameInput.setText(MyApplication.getInstance().getUser().getNickname());
        if ("1".equals(MyApplication.getInstance().getUser().getGender())) {
            mManBtn.setChecked(true);
            sex = 1;
        } else {
            mWomanBtn.setChecked(true);
            sex = 2;
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userModel = new UserModel(this);
    }

    public void click(View view) {
        UMEventUtil.onEvent(this, UMEvent.setpersonchangeinfosave);
        sexChose();
        final String name = mNameInput.getText().toString().trim();
        if (verfiChange(name))
            return;
        showProgress();
        userModel.changePersonInfo(MyApplication.getInstance().getTel(), MyApplication.getInstance().getUser().getUser_name(), name, String.valueOf(sex), new Callback<Result>() {
            @Override
            public void onSuccess(Result data) {
                closeProgress();
                MyApplication.getInstance().getUser().setNickname(name);
                MyApplication.getInstance().getUser().setGender(String.valueOf(sex));
//                if (MyApplication.getInstance().getStore().isShopkeeper()) {
//                    MyApplication.getInstance().getStore().setContacts(name);
//                }
                showToast("信息修改成功");
                setResult(RESULT_OK);
                finish();

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    private boolean verfiChange(String name) {
        if (TextUtils.isEmpty(name)) {
            showToast("请输入名字!");
            return true;
        }
        if (name.equals(MyApplication.getInstance().getUser().getNickname()) && sex == Integer.parseInt(MyApplication.getInstance().getUser().getGender())) {
            showToast("您未做任何修改!");
            return true;
        }
        return false;
    }

    private void sexChose() {
        if (mWomanBtn.isChecked()) {
            sex = 2;
        }
        if (mManBtn.isChecked()) {
            sex = 1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this,UMEvent.setpersonchangeinfoback);
    }
}
