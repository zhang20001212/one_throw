package com.onetoall.yjt.controller.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.MainActivity;
import com.onetoall.yjt.core.BaseListActivity;
import com.onetoall.yjt.domain.AllInfoNewBean;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.UserModel;
import com.qw.framework.widget.pull.BaseViewHolder;
import com.qw.framework.widget.pull.PullRecyclerView;

/**
 * Created by user on 2016/11/18.
 *   setTitle("选择门店");
 *     setContentView(R.layout.activity_chose_store,true);
 */

public class ChoseStoreActivity extends BaseListActivity<AllInfoNewBean.StoreArrBean> {
    private UserModel mUserModel;


    @Override
    protected void setContentView() {
        super.setContentView();
        setContentView(R.layout.activity_chose_store,true);
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("选择门店");
        mPullRecyclerView.setEnablePullToEnd(false);
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(this).inflate(R.layout.item_chose_store_layout, parent, false));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mUserModel = new UserModel(this);
        loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
    }
    private void loadDataFromServer(final int mode){
        mUserModel.querryAllInfoNew(MyApplication.getInstance().getTel(), new Callback<AllInfoNewBean>() {
            @Override
            public void onSuccess(AllInfoNewBean data) {
                if (mode==PullRecyclerView.MODE_PULL_TO_START){
                    modules.clear();

                }
                modules.addAll(data.getStore_arr());
                adapter.notifyDataSetChanged();
                mPullRecyclerView.onRefreshCompleted(mode);
            }

            @Override
            public void onFailure(int code, String msg) {
                mPullRecyclerView.onRefreshCompleted(mode);
            }
        });
    }


    class MyViewHolder extends BaseViewHolder implements View.OnClickListener{

        private  TextView mStoreName;
        private  TextView mStoreN0;
        private  RelativeLayout choseStoreBtn;
        private AllInfoNewBean.StoreArrBean storeArrBean;


        public MyViewHolder(View itemView) {
            super(itemView);
             mStoreName = (TextView) itemView.findViewById(R.id.pay_tv);
             mStoreN0 = (TextView) itemView.findViewById(R.id.pay_people);
             choseStoreBtn = (RelativeLayout) itemView.findViewById(R.id.ali_pay_btn);
            choseStoreBtn.setOnClickListener(this);
        }

        @Override
        public void bindData(int i) {
            storeArrBean = modules.get(i);
            mStoreN0.setText("门店编号："+storeArrBean.getStore_number());
            mStoreName.setText("门店名："+storeArrBean.getStore_name());
        }

        @Override
        public void onClick(View view) {
//            private long store_id;
//            private String store_number;
//            private String store_name;
//            private String client;
//            private long merchant_id;
//            private String lev;
            MyApplication.getInstance().getStore().setStore_id(storeArrBean.getStore_id());
            MyApplication.getInstance().getStore().setStore_name(storeArrBean.getStore_name());
            MyApplication.getInstance().getStore().setStore_name(storeArrBean.getStore_name());
            MyApplication.getInstance().getStore().setClient(storeArrBean.getClient());
            MyApplication.getInstance().getStore().setMerchant_id(storeArrBean.getMerchant_id());
            MyApplication.getInstance().getStore().setLev(storeArrBean.getLev());
            setResult(RESULT_OK);
            startActivity(new Intent(ChoseStoreActivity.this, MainActivity.class));
            finishActivity();
        }
    }

    @Override
    public void onRefresh(int mode) {
        loadDataFromServer(mode);
    }
}
