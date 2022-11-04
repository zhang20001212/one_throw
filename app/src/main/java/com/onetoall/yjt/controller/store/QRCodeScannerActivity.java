package com.onetoall.yjt.controller.store;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.pay.CaptureActivityActivity;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.qw.framework.utils.NetworkUtils;

/**
 * Created by qinwei on 2016/11/8 16:30
 * email:qinwei_it@163.com
 */

public class QRCodeScannerActivity extends CaptureActivityActivity {
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_qr_code_scanner, true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_bind_qr_code));
    }

    @Override
    public void handleDecode(Result result) {
        super.handleDecode(result);
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            if (!NetworkUtils.isNetworkConnected(this)) {
                showToast(getResources().getString(R.string.no_network));
                finish();
                return;
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.ARG_QR_CODE_INFO, resultString);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMEventUtil.onEvent(this, UMEvent.shoppayaddback);
    }
}
