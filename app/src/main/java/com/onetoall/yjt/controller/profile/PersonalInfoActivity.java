package com.onetoall.yjt.controller.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.PersonalInfomationBean;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.UserModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.row.BaseRowDescriptor;
import com.onetoall.yjt.widget.row.ContainerDescriptor;
import com.onetoall.yjt.widget.row.ContainerView;
import com.onetoall.yjt.widget.row.GroupDescriptor;
import com.onetoall.yjt.widget.row.OnRowClickListener;
import com.onetoall.yjt.widget.row.expand.IOSRowDescriptor;
import com.onetoall.yjt.widget.row.tool.RowActionEnum;
import com.qw.framework.utils.TimeHelper;

import java.util.ArrayList;

/**
 * 个人信息
 * Created by user on 2016/11/4.
 */

public class PersonalInfoActivity extends BaseActivity implements OnRowClickListener {

    private TextView mStoreNameLabel;
    private TextView mUserNameLabel;
    private TextView mUserJobNameLabel;
    private TextView mPersonalInfoSex;
    private TextView mPersonalInfoAccount;
    private TextView mPersonalInfoAddTime;
    private TextView mPersonalInfoShop;
    private TextView mPersonalInfoArea;
    private TextView mPersonalInfoAddress;
    private ContainerView mContainerView;
    private UserModel userModel;

    private void assignViews() {
        mStoreNameLabel = (TextView) findViewById(R.id.mStoreNameLabel);
        mUserNameLabel = (TextView) findViewById(R.id.mUserNameLabel);
        mUserJobNameLabel = (TextView) findViewById(R.id.mUserJobNameLabel);
        mPersonalInfoSex = (TextView) findViewById(R.id.mPersonalInfoSex);
        mPersonalInfoAccount = (TextView) findViewById(R.id.mPersonalInfoAccount);
        mPersonalInfoAddTime = (TextView) findViewById(R.id.mPersonalInfoAddTime);
        mPersonalInfoShop = (TextView) findViewById(R.id.mPersonalInfoShop);
        mPersonalInfoArea = (TextView) findViewById(R.id.mPersonalInfoArea);
        mPersonalInfoAddress = (TextView) findViewById(R.id.mPersonalInfoAddress);
        mContainerView = (ContainerView) findViewById(R.id.mContainerView);
    }

    @Override
    protected void setContentView() {
            setContentView(R.layout.activity_personal_info_layout,true);
    }

    @Override
    protected void initView() {
        setTitle("个人信息");
        assignViews();
        ContainerView mContainerView = (ContainerView) findViewById(R.id.mContainerView);

        ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();
        ArrayList<BaseRowDescriptor> rowDescriptors1 = new ArrayList<BaseRowDescriptor>();
        rowDescriptors1.add(new IOSRowDescriptor(0, "修改密码", RowActionEnum.ACTION_CHANGEPWD));
        GroupDescriptor groupDescriptor1 = new GroupDescriptor("", rowDescriptors1);

        ArrayList<BaseRowDescriptor> rowDescriptors2 = new ArrayList<BaseRowDescriptor>();
        rowDescriptors2.add(new IOSRowDescriptor(0, "修改个人信息", RowActionEnum.ACTION_CHANGEINFO));
        GroupDescriptor groupDescriptor2 = new GroupDescriptor("", rowDescriptors2);


        groupDescriptors.add(groupDescriptor1);
        groupDescriptors.add(groupDescriptor2);
        ContainerDescriptor containerDescriptor = new ContainerDescriptor(groupDescriptors);
        mContainerView.initializeData(containerDescriptor, this);
        mContainerView.notifyDataChanged();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userModel = new UserModel(this);
        initUserInfo();
        initLinstener();
    }

    /**
     * 初始化个人信息
     */
    private void initUserInfo() {
        mStoreNameLabel.setText("门店:"+ MyApplication.getInstance().getStore().getStore_name());
        mUserNameLabel.setText("姓名:"+MyApplication.getInstance().getUser().getNickname());
        mUserJobNameLabel.setText(MyApplication.getInstance().getStore().getJobName());
        mPersonalInfoSex.setText("性别:"+("1".equals(MyApplication.getInstance().getUser().getGender())?"男":"女"));
        mPersonalInfoAccount.setText("账号:"+MyApplication.getInstance().getUser().getMobile());
        mPersonalInfoAddTime.setText("添加时间:"+ TimeHelper.getDate(Long.parseLong(MyApplication.getInstance().getUser().getCrdate()+"000")));
        userModel.selectStoreInfo(MyApplication.getInstance().getStore().getStore_id() + "", new Callback<PersonalInfomationBean>() {
            @Override
            public void onSuccess(PersonalInfomationBean data) {
                mPersonalInfoArea.setText("所属区域:"+data.getArea());
                mPersonalInfoAddress.setText("门店地址:"+data.getAddress());
                mPersonalInfoShop.setText("商户名:"+data.getMerchant_name() );
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    private void initLinstener(){

    }

    @Override
    public void onRowClick(View rowView, RowActionEnum action) {
            switch (action){

                case ACTION_CHANGEPWD://修改密码
                    UMEventUtil.onEvent(this, UMEvent.setpersonchangepass);
                    startActivity(new Intent(PersonalInfoActivity.this,PersonPwdChangeActivity.class));
                    break;
                case ACTION_CHANGEINFO://修改个人信息
                    UMEventUtil.onEvent(this, UMEvent.setpersonchangeinfo);
                    startActivityForResult(new Intent(PersonalInfoActivity.this,PersonInfoChangeActivity.class), Constants.PERSON_CHANGE_USERINFO_CODE);
                    break;
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Constants.PERSON_CHANGE_USERINFO_CODE){
            mUserNameLabel.setText("姓名:"+MyApplication.getInstance().getUser().getNickname());
            mPersonalInfoSex.setText("性别:"+("1".equals(MyApplication.getInstance().getUser().getGender())?"男":"女"));
        }
    }
}
