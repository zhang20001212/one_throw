package com.onetoall.yjt.controller.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.QRCodeInfo;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.QRCodeModel;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.row.BaseRowDescriptor;
import com.onetoall.yjt.widget.row.ContainerDescriptor;
import com.onetoall.yjt.widget.row.ContainerView;
import com.onetoall.yjt.widget.row.GroupDescriptor;
import com.onetoall.yjt.widget.row.OnRowClickListener;
import com.onetoall.yjt.widget.row.expand.IOSRowDescriptor;
import com.onetoall.yjt.widget.row.tool.RowActionEnum;

import java.util.ArrayList;

import static com.onetoall.yjt.R.string.weixin;

/**
 * Created by qinwei on 2016/10/23 16:53
 * email:qinwei_it@163.com
 */

public class PayChannelActivity extends BaseActivity implements OnRowClickListener {
    private ContainerView mContainerView;
    private QRCodeModel mQrCodeModel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pay_channel, true);
    }

    @Override
    protected void initView() {
        mContainerView = (ContainerView) findViewById(R.id.mContainerView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("支付渠道");
        mQrCodeModel = new QRCodeModel(this);
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        mQrCodeModel.loadQRCodes(MyApplication.getInstance().getStore().getStore_id() + "", new Callback<ArrayList<QRCodeInfo>>() {
            @Override
            public void onSuccess(ArrayList<QRCodeInfo> data) {
                setPayChannel(data);
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    @Override
    public void onRowClick(View rowView, RowActionEnum action) {
        switch (action) {
            case ACTION_QRCODE_MANAGER:
                UMEventUtil.onEvent(this, UMEvent.shoppaysmf);
                startActivity(new Intent(this, QRCodeManagerActivity.class));
                break;
            default:
                break;
        }
    }

    public void setPayChannel(ArrayList<QRCodeInfo> payChannel) {
        ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();
        ArrayList<BaseRowDescriptor> rowDescriptors1 = new ArrayList<BaseRowDescriptor>();
        rowDescriptors1.add(new IOSRowDescriptor(R.drawable.ic_collect_money_type_alipay, getString(R.string.alipay), null));
        rowDescriptors1.add(new IOSRowDescriptor(R.drawable.ic_collect_money_type_winxin, getString(weixin), null));
        if (payChannel.size() > 0) {
            rowDescriptors1.add(new IOSRowDescriptor(R.drawable.pay_icon_3, "已开通", RowActionEnum.ACTION_QRCODE_MANAGER));
        } else {
            if (MyApplication.getInstance().getStore().isShopkeeper()) {
                rowDescriptors1.add(new IOSRowDescriptor(R.drawable.pay_icon_3, "未开通", RowActionEnum.ACTION_QRCODE_MANAGER));
            } else {
                rowDescriptors1.add(new IOSRowDescriptor(R.drawable.pay_icon_3, "未开通", null));
            }
        }
        GroupDescriptor groupDescriptor1 = new GroupDescriptor("", rowDescriptors1);

        groupDescriptors.add(groupDescriptor1);
        ContainerDescriptor containerDescriptor = new ContainerDescriptor(groupDescriptors);
        mContainerView.initializeData(containerDescriptor, this);
        mContainerView.notifyDataChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this,UMEvent.shoppaytypeback);
    }
}
