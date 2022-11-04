package com.onetoall.yjt.controller.store;

import android.os.Bundle;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseRefreshActivity;
import com.onetoall.yjt.domain.Store;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.StoreModel;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.RefreshView;

public class StoreDetailInfoActivity extends BaseRefreshActivity {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView sureBtn;
    private StoreModel mStoreModel;


    @Override
    protected void setContentView() {

        setContentView(R.layout.activity_store_detail_info, true);
    }

    @Override
    protected void initView() {
        super.initView();
        tv1 = (TextView) findViewById(R.id.tv_1);
        tv2 = (TextView) findViewById(R.id.tv_2);
        tv3 = (TextView) findViewById(R.id.tv_3);
        tv4 = (TextView) findViewById(R.id.tv_4);
        tv5 = (TextView) findViewById(R.id.tv_5);
        tv6 = (TextView) findViewById(R.id.tv_6);
        tv7 = (TextView) findViewById(R.id.tv_7);
        tv8 = (TextView) findViewById(R.id.tv_8);
        tv9 = (TextView) findViewById(R.id.tv_9);
        tv10 = (TextView) findViewById(R.id.tv_10);
        sureBtn = (TextView) findViewById(R.id.sure_btn);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_store_info));
        mStoreModel = new StoreModel(this);
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        String storeId = MyApplication.getInstance().getStore().getStore_id() + "";
        String tel = MyApplication.getInstance().getTel();
        String storeNumber = MyApplication.getInstance().getStore().getStore_number();
        mStoreModel.loadStoreInfo(storeId, tel, storeNumber, new Callback<Store>() {


            @Override
            public void onSuccess(Store data) {
                setStoreInfo(data);
            }

            @Override
            public void onFailure(int code, String msg) {
                changeRefreshState(RefreshView.State.error);
            }
        });
    }

    public void setStoreInfo(Store storeInfo) {
        tv1.setText(storeInfo.getMerchant_name());
        tv2.setText(storeInfo.getMerchant_number());
        tv3.setText(storeInfo.getStore_name());
        tv4.setText(storeInfo.getStore_number());
        tv5.setText(storeInfo.getBusiness_area());
        tv6.setText(storeInfo.getArea());
        tv7.setText(storeInfo.getAddress());
        tv8.setText("联系人: " + storeInfo.getContacts());
        tv9.setText("联系电话: " + storeInfo.getContacts_phone());
        tv10.setText("邮箱: " + storeInfo.getMailbox());
        changeRefreshState(RefreshView.State.done);
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
        UMEventUtil.onEvent(this, UMEvent.shopinfoback);
    }
}
