package com.onetoall.yjt.controller.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
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

import java.util.ArrayList;

import static com.onetoall.yjt.R.string.weixin;

/**
 * Created by qinwei on 2016/10/18 15:28
 * email:qinwei_it@163.com
 */

public class CollectMoneyTypeActivity extends BaseActivity implements OnRowClickListener {
    private ContainerView mContainerView;
    private static final String COLLECT_PRICE_TOTAL = "COLLECT_PRICE_TOTAL";//收钱
    private double totalPrice;//收钱总额

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_collect_money_type, true);
    }

    @Override
    protected void initView() {
        mContainerView = (ContainerView) findViewById(R.id.mContainerView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        totalPrice = getIntent().getDoubleExtra(COLLECT_PRICE_TOTAL, 0.0d);
        setTitle("收款");
        ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();
        ArrayList<BaseRowDescriptor> rowDescriptors1 = new ArrayList<BaseRowDescriptor>();
        rowDescriptors1.add(new IOSRowDescriptor(R.drawable.ic_collect_money_type_alipay, getString(R.string.alipay), RowActionEnum.ACTION_COLLECT_MONEY_ALIPAY));
        rowDescriptors1.add(new IOSRowDescriptor(R.drawable.ic_collect_money_type_winxin, getString(weixin), RowActionEnum.ACTION_COLLECT_MONEY_WINXIN));
        GroupDescriptor groupDescriptor1 = new GroupDescriptor("", rowDescriptors1);

        ArrayList<BaseRowDescriptor> rowDescriptors2 = new ArrayList<BaseRowDescriptor>();
        rowDescriptors2.add(new IOSRowDescriptor(R.drawable.ic_collect_money_type_money, getString(R.string.money), RowActionEnum.ACTION_COLLECT_MONEY));
        GroupDescriptor groupDescriptor2 = new GroupDescriptor("收款记账", rowDescriptors2);


        groupDescriptors.add(groupDescriptor1);
        groupDescriptors.add(groupDescriptor2);
        ContainerDescriptor containerDescriptor = new ContainerDescriptor(groupDescriptors);
        mContainerView.initializeData(containerDescriptor, this);
        mContainerView.notifyDataChanged();
    }

    @Override
    public void onRowClick(View rowView, RowActionEnum action) {
        switch (action) {
            case ACTION_COLLECT_MONEY_ALIPAY:
                UMEventUtil.onEvent(this, UMEvent.payalipay);
                Intent alipay = new Intent(this, QRAlipayActivity.class);
                alipay.putExtra(Constants.ARG_PAY_PRICE, totalPrice + "");
                startActivity(alipay);
                break;
            case ACTION_COLLECT_MONEY_WINXIN:
                UMEventUtil.onEvent(this, UMEvent.paywechat);
                Intent weixin = new Intent(this, QRWeixinActivity.class);
                weixin.putExtra(Constants.ARG_PAY_PRICE, totalPrice + "");
                startActivity(weixin);
                break;
            case ACTION_COLLECT_MONEY:
                UMEventUtil.onEvent(this, UMEvent.paycash);
                startActivity(new Intent(this, PayCashActivity.class).putExtra(COLLECT_PRICE_TOTAL, totalPrice));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this, UMEvent.payback);
    }
}
