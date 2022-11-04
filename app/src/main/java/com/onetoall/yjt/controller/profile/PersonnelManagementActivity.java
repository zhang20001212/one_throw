package com.onetoall.yjt.controller.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseRefreshActivity;
import com.onetoall.yjt.domain.PersonnelManagementBean;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.PersonnelManagementModel;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.RefreshView;

/**
 * Created by shaomengjie on 2016/11/21.
 */

public class PersonnelManagementActivity extends BaseRefreshActivity {

    private ImageView icon;
    private LinearLayout lin;
    private LayoutInflater layoutInflater;
    private TextView mendianTv;
    private TextView nameTv;
    private TextView zhiweiTv;

    private PersonnelManagementModel personnelManagementModel;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_personnel_management,true);
    }

    @Override
    protected void initView() {
        super.initView();
        layoutInflater = LayoutInflater.from(this);
        lin = (LinearLayout) findViewById(R.id.lin);
        icon = (ImageView) findViewById(R.id.icon);
        mendianTv = (TextView) findViewById(R.id.mendian_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        zhiweiTv = (TextView) findViewById(R.id.zhiwei_tv);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("人员管理");
        personnelManagementModel = new PersonnelManagementModel(this);
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        personnelManagementModel.loadPersonnelManagement(
                MyApplication.getInstance().getTel(),
                MyApplication.getInstance().getStore().getStore_number(),
                new Callback<PersonnelManagementBean>() {
            @Override
            public void onSuccess(PersonnelManagementBean data) {
                changeRefreshState(RefreshView.State.done);
                if (null != data) {
                    setHead(data);
                    if (data.getAccount_arr().size() == 0) {
                        showToast("暂无数据");
                    } else {
                        lin.removeAllViews();
                    }
                    for (PersonnelManagementBean.AccountArrBean a : data.getAccount_arr()) {
                        if (a.getSub_account().getStore_id().equals(MyApplication.getInstance().getStore().getStore_number())) {
                            setNewItem(a.getSub_account().getNickname(), a.getSub_account().getMobile(), a.getSub_account().getMobile());
                        }
                    }
                } else {
                    changeRefreshState(RefreshView.State.ing);
                    loadDataFromServer();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                changeRefreshState(RefreshView.State.error);
            }
        });
    }

    private void setHead(PersonnelManagementBean data) {
        mendianTv.setText("门店: " +MyApplication.getInstance().getStore().getStore_name());
        nameTv.setText("姓名: " + data.getUser_info().getNickname());
        zhiweiTv.setText("职位: " + "店长");
    }

    void setNewItem(String namee, String accountt, String phonee) {
        View vi = layoutInflater.inflate(R.layout.activity_personnel_management_include, null);
        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView zhanghao = (TextView) vi.findViewById(R.id.zhanghao);
        TextView phone = (TextView) vi.findViewById(R.id.phone);
//        TextView dele_btn = (TextView) vi.findViewById(R.id.dele_btn);
//        TextView edit_btn = (TextView) vi.findViewById(R.id.edit_btn);
        name.setText("昵称:" + namee);
        zhanghao.setText("账号:" + accountt);
        phone.setText("手机:" + phonee);
        lin.addView(vi);
    }

    @Override
    public void onRetry() {
        super.onRetry();
        changeRefreshState(RefreshView.State.ing);
        loadDataFromServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this, UMEvent.setctrlpersonback);
    }
}
