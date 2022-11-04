package com.onetoall.yjt.controller.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.MainActivity;
import com.onetoall.yjt.core.BaseRefreshActivity;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.AccountBookModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.RefreshView;
import com.qw.framework.utils.TimeHelper;

/**
 * Created by qinwei on 2016/10/27 21:02
 * email:qinwei_it@163.com
 */

public class PayOrderDetailActivity extends BaseRefreshActivity implements View.OnClickListener {
    private String orderId;
    private AccountBookModel mAccountBookModel;
    private ImageView statusImg;
    private TextView statusTv;
    private TextView mDetailAmountLabel;
    private TextView mDetailCreateTimeLabel;
    private TextView mDetailPayTypeLabel;
    private TextView mDetailPayUsernameLabel;
    private TextView mDetailCollectMoneyUsernameLabel;
    private TextView mDetailPayAccountLabel;
    private TextView mDetailOrderNumberLabel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pay_order_detail, true);
    }

    @Override
    protected void initView() {
        super.initView();
        statusImg = (ImageView) findViewById(R.id.status_img);
        statusTv = (TextView) findViewById(R.id.status_tv);
        mDetailAmountLabel = (TextView) findViewById(R.id.mDetailAmountLabel);
        mDetailCreateTimeLabel = (TextView) findViewById(R.id.mDetailCreateTimeLabel);
        mDetailPayTypeLabel = (TextView) findViewById(R.id.mDetailPayTypeLabel);
        mDetailPayUsernameLabel = (TextView) findViewById(R.id.mDetailPayUsernameLabel);
        mDetailCollectMoneyUsernameLabel = (TextView) findViewById(R.id.mDetailCollectMoneyUsernameLabel);
        mDetailPayAccountLabel = (TextView) findViewById(R.id.mDetailPayAccountLabel);
        mDetailOrderNumberLabel = (TextView) findViewById(R.id.mDetailOrderNumberLabel);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(R.string.title_pay_detail);
        orderId = getIntent().getStringExtra(Constants.ARG_PAY_ORDER_ID);
        mAccountBookModel = new AccountBookModel(this);
        loadDataFromServer();
    }


    private void loadDataFromServer() {
        mAccountBookModel.loadPayOrderById(orderId, new Callback<PayOrderDetail>() {
            @Override
            public void onSuccess(PayOrderDetail data) {
                setPayOrderDetail(data);
            }

            @Override
            public void onFailure(int code, String msg) {
                changeRefreshState(RefreshView.State.error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void setPayOrderDetail(PayOrderDetail data) {
        mDetailAmountLabel.setText(data.getGood_amount() + "");
        mDetailCreateTimeLabel.setText(TimeHelper.updateMilliSecToFormatDateStr(data.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
        mDetailPayTypeLabel.setText(data.getPayUserName());
        mDetailPayUsernameLabel.setText(data.getBuyer());
        mDetailCollectMoneyUsernameLabel.setText(data.getCashier());
        mDetailPayAccountLabel.setText(data.getTrans_account());
        mDetailOrderNumberLabel.setText(data.getTrans_order());
        changeRefreshState(RefreshView.State.done);
        if (data.getStatus() == 1) {
            statusImg.setImageResource(R.drawable.ic_pay_success);
            statusTv.setText(R.string.mCollectMoneySuccessLabel);
        } else {
            statusImg.setImageResource(R.drawable.ic_pay_faile);
            statusTv.setText(R.string.mCollectMoneyFailLabel);
        }
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
        UMEventUtil.onEvent(this,UMEvent.acpaydetailback);
    }
}
