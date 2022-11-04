package com.onetoall.yjt.controller.store;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.QRCodeInfo;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.ImageDisplay;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;

/**
 * 什码付详情
 * Created by qinwei on 2016/11/8 16:02
 * email:qinwei_it@163.com
 */

public class QRCodeDetailActivity extends BaseActivity {
    private ImageView mQRCodeContentImg;
    private TextView mQRCodeNumberLabel;
    private TextView mQRCodeCreateTimeLabel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_qr_code_detail, true);
    }

    @Override
    protected void initView() {
        mQRCodeContentImg = (ImageView) findViewById(R.id.mQRCodeContentImg);
        mQRCodeNumberLabel = (TextView) findViewById(R.id.mQRCodeNumberLabel);
        mQRCodeCreateTimeLabel = (TextView) findViewById(R.id.mQRCodeCreateTimeLabel);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_qr_shenma));
        QRCodeInfo qrCodeInfo = (QRCodeInfo) getIntent().getSerializableExtra(Constants.ARG_QR_CODE_INFO);
        mQRCodeNumberLabel.setText("编号: " + qrCodeInfo.getQrcode_id());
        mQRCodeCreateTimeLabel.setText("绑定时间: " + qrCodeInfo.getBinding_time());
        ImageDisplay.getInstance().displayImage(qrCodeInfo.getUrl(), mQRCodeContentImg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this, UMEvent.shoppaysmfback);
    }
}
