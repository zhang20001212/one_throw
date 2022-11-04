package com.onetoall.yjt.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.store.ChoseStoreActivity;
import com.onetoall.yjt.controller.store.CustomerManagerActivity;
import com.onetoall.yjt.controller.store.PayChannelActivity;
import com.onetoall.yjt.controller.store.StoreDetailInfoActivity;
import com.onetoall.yjt.core.BaseFragment;
import com.onetoall.yjt.domain.Store;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.model.impl.StoreModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.QToolbar;
import com.onetoall.yjt.widget.row.BaseRowDescriptor;
import com.onetoall.yjt.widget.row.ContainerDescriptor;
import com.onetoall.yjt.widget.row.ContainerView;
import com.onetoall.yjt.widget.row.GroupDescriptor;
import com.onetoall.yjt.widget.row.OnRowClickListener;
import com.onetoall.yjt.widget.row.expand.IOSRowDescriptor;
import com.onetoall.yjt.widget.row.tool.RowActionEnum;
import com.onetoall.yjt.widget.tab.ITabFragment;

import java.util.ArrayList;

/**
 * 店铺
 * Created by qinwei on 2016/10/18 13:40
 * email:qinwei_it@163.com
 */

public class StoreFragment extends BaseFragment implements ITabFragment, OnRowClickListener, SwipeRefreshLayout.OnRefreshListener {
    private QToolbar toolbar;
    private ContainerView mContainerView;
    private LinearLayout mStoreInfoLayout;
    private TextView mStoreShopNameLabel;
    private TextView mStoreNameLabel;
    private TextView mStoreShopOwnerLabel;
    private StoreModel mStoreModel;
    private Button mStoreChoseBtn;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_store;
    }


    @Override
    protected void initView(View v) {
        toolbar = (QToolbar) v.findViewById(R.id.toolbar);
        mContainerView = (ContainerView) v.findViewById(R.id.mContainerView);
        mStoreInfoLayout = (LinearLayout) v.findViewById(R.id.mStoreInfoLayout);
        mStoreShopNameLabel = (TextView) v.findViewById(R.id.mStoreShopNameLabel);
        mStoreNameLabel = (TextView) v.findViewById(R.id.mStoreNameLabel);
        mStoreShopOwnerLabel = (TextView) v.findViewById(R.id.mStoreShopOwnerLabel);
        mStoreChoseBtn = (Button) v.findViewById(R.id.mStoreChoseBtn);
        mSwipeRefreshLayout = ((SwipeRefreshLayout) v.findViewById(R.id.mSwipeRefreshLayout));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        if (MyApplication.getInstance().getStoreNumber() == 1) {
            mStoreChoseBtn.setVisibility(View.GONE);
        }
        mStoreInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMEventUtil.onEvent(StoreFragment.this.getActivity(), UMEvent.shopinfo);
                startActivity(new Intent(getActivity(), StoreDetailInfoActivity.class));
            }
        });
        mStoreChoseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoiceStore();
            }
        });
    }

    public void showChoiceStore() {
        new AlertDialog.Builder(getActivity()).setTitle("切换门店")
                .setCancelable(false)
                .setMessage("确认切换门店吗？")
                .setNegativeButton(R.string.Sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(getActivity(), ChoseStoreActivity.class), Constants.PERSON_CHANGE_STORE);
                    }
                })
                .setPositiveButton(R.string.cancel, null).show();
    }

    @Override
    protected void initData() {
        super.initData();
        toolbar.setTitle(R.string.app_home_shop_label);
        mStoreModel = new StoreModel((OnBaseModelListener) getActivity());
        ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();
        ArrayList<BaseRowDescriptor> rowDescriptors1 = new ArrayList<BaseRowDescriptor>();
        rowDescriptors1.add(new IOSRowDescriptor(R.drawable.ic_pay, "支付渠道", RowActionEnum.ACTION_PAY_CHANNEL));
        GroupDescriptor groupDescriptor1 = new GroupDescriptor("", rowDescriptors1);

        ArrayList<BaseRowDescriptor> rowDescriptors2 = new ArrayList<BaseRowDescriptor>();
        rowDescriptors2.add(new IOSRowDescriptor(R.drawable.ic_people, "客户管理", RowActionEnum.ACTION_CUSTOMER));
        GroupDescriptor groupDescriptor2 = new GroupDescriptor("", rowDescriptors2);


        groupDescriptors.add(groupDescriptor1);
        groupDescriptors.add(groupDescriptor2);
        ContainerDescriptor containerDescriptor = new ContainerDescriptor(groupDescriptors);
        mContainerView.initializeData(containerDescriptor, this);
        mContainerView.notifyDataChanged();

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
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int code, String msg) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onMenuItemClick(int id) {

    }

    @Override
    public void onRowClick(View rowView, RowActionEnum action) {
        switch (action) {
            case ACTION_PAY_CHANNEL:
                UMEventUtil.onEvent(StoreFragment.this.getActivity(), UMEvent.shoppaytype);
                startActivity(new Intent(getActivity(), PayChannelActivity.class));
                break;
            case ACTION_CUSTOMER:
                UMEventUtil.onEvent(getContext(), UMEvent.shopcustomerctrl);
                startActivity(new Intent(getActivity(), CustomerManagerActivity.class));
            default:
                break;
        }
    }

    public void setStoreInfo(Store storeInfo) {
        mStoreShopNameLabel.setText("商户: " + storeInfo.getMerchant_name());
        mStoreNameLabel.setText("门店: " + storeInfo.getStore_name());
        mStoreShopOwnerLabel.setText("店长: " + storeInfo.getContacts());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getInstance().getStore().getContacts() != null) {
            mStoreShopOwnerLabel.setText("店长: " + MyApplication.getInstance().getStore().getContacts());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PERSON_CHANGE_STORE) {
            loadDataFromServer();
        }
    }

    @Override
    public void onRefresh() {
        loadDataFromServer();
    }
}
