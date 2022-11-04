package com.onetoall.yjt.controller.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.MainActivity;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.TextToSpeechUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qinwei on 2016/10/27 21:02
 * email:qinwei_it@163.com
 */

public class PaySuccessActivity extends BaseActivity implements View.OnClickListener {
    private ImageView statusImg;
    private TextView statusTv;
    private TextView mDetailAmountLabel;
    private TextView mDetailOrderAmountLabel;
    private TextView mDetailCreateTimeLabel;
    private TextView mDetailPayTypeLabel;
    private TextView mDetailPayUsernameLabel;
    private TextView mDetailCollectMoneyUsernameLabel;
    private TextView mDetailPayAccountLabel;
    private TextView mDetailOrderNumberLabel;
    private Button sureBtn;
    private PayOrderDetail mPayOrderDetail;
    private TextToSpeechUtils textToSpeechUtils;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_pay_success, true);
    }

    @Override
    protected void initView() {
        statusImg = (ImageView) findViewById(R.id.status_img);
        statusTv = (TextView) findViewById(R.id.status_tv);
        mDetailAmountLabel = (TextView) findViewById(R.id.mDetailAmountLabel);
        mDetailOrderAmountLabel = (TextView) findViewById(R.id.mDetailOrderAmountLabel);
        mDetailCreateTimeLabel = (TextView) findViewById(R.id.mDetailCreateTimeLabel);
        mDetailPayTypeLabel = (TextView) findViewById(R.id.mDetailPayTypeLabel);
        mDetailPayUsernameLabel = (TextView) findViewById(R.id.mDetailPayUsernameLabel);
        mDetailCollectMoneyUsernameLabel = (TextView) findViewById(R.id.mDetailCollectMoneyUsernameLabel);
        mDetailPayAccountLabel = (TextView) findViewById(R.id.mDetailPayAccountLabel);
        mDetailOrderNumberLabel = (TextView) findViewById(R.id.mDetailOrderNumberLabel);
        sureBtn = (Button) findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(this);
        textToSpeechUtils = new TextToSpeechUtils(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(R.string.title_pay_detail);
        mPayOrderDetail = (PayOrderDetail) getIntent().getSerializableExtra(Constants.ARG_PAY_ORDER_DETAIL_ENTITY);
        setPayOrderDetail(mPayOrderDetail);
    }

    public String getTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return myFormatter.format(date);
    }


    @Override
    public void onClick(View v) {
        goHome();
    }

    @Override
    public void finishActivity() {
        goHome();
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.KEY_ACTION, Constants.ACTION_BACK_TO_HOME);
        startActivity(intent);
    }

    public void setPayOrderDetail(PayOrderDetail data) {
        if (data == null) return;
        mDetailAmountLabel.setText(data.getGood_amount() + "");
        mDetailOrderAmountLabel.setText(data.getGood_amount() + "");
        mDetailCreateTimeLabel.setText(data.getTrans_time());
        mDetailPayTypeLabel.setText(data.getPayment_type());
        mDetailPayUsernameLabel.setText(data.getBuyer());
        mDetailCollectMoneyUsernameLabel.setText(data.getCashier());
        mDetailPayAccountLabel.setText(data.getTrans_account());
        mDetailOrderNumberLabel.setText(data.getTrans_order());
        if (data.getTotal_amount() > 0) {
            statusImg.setImageResource(R.drawable.ic_pay_success);
            statusTv.setText(R.string.mCollectMoneySuccessLabel);
            textToSpeechUtils.notifyNewMessage("收款成功,金额"+data.getGood_amount()+"元");
        } else {
            statusImg.setImageResource(R.drawable.ic_pay_faile);
            statusTv.setText(R.string.mCollectMoneyFailLabel);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goHome();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textToSpeechUtils.release();
    }
}
