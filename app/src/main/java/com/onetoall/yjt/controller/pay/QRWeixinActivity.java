package com.onetoall.yjt.controller.pay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.Result;
import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.dialog.WeixinQRCodeGuideDialog;
import com.onetoall.yjt.dialog.WeixinQRScannerGuideDialog;
import com.onetoall.yjt.domain.PayOrder;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.domain.PayRequestParameter;
import com.onetoall.yjt.fragment.pay.WeiXinQRCodeFragment;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.PayModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.tab.v2.TabLayout;
import com.onetoall.yjt.widget.tab.v2.TabView;
import com.onetoall.yjt.zxing.CameraManager;
import com.qw.framework.utils.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by qinwei on 2016/10/25 14:48
 * email:qinwei_it@163.com
 */

public class QRWeixinActivity extends CaptureActivityActivity implements TabLayout.OnTabClickListener {
    private TabLayout mTabLayoutV2;
    private int currentIndex = -1;
    private boolean dialogIsShowing;
    private TextView mQrAlipayGuideHintLabel;
    private PayModel mPayModel;
    private String price;
    private boolean isLoading;
    private PayRequestParameter parameter;
    private TextView mQrAlipayPriceLabel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_qr_weixin, true);
    }

    @Override
    protected void initView() {
        super.initView();
        mQrAlipayGuideHintLabel = (TextView) findViewById(R.id.mQrAlipayGuideHintLabel);
        mQrAlipayPriceLabel = (TextView) findViewById(R.id.mQrAlipayPriceLabel);
        mTabLayoutV2 = (TabLayout) findViewById(R.id.mTabLayoutV2);
        ArrayList<TabView.Tab> tabs = new ArrayList<>();
        tabs.add(new TabView.Tab(R.drawable.select_qr_scan, R.string.qr_scanner, R.color.selector_tab_label, null));
        tabs.add(new TabView.Tab(R.drawable.select_qr_code, R.string.qr_code, R.color.selector_tab_label, null));
        mTabLayoutV2.initData(tabs, this);
        mTabLayoutV2.setCurrentTab(0);
        findViewById(R.id.mQrAlipayGuideLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMEventUtil.onEvent(QRWeixinActivity.this, UMEvent.wechatscodefun);
                if (currentIndex == 0) {
                    dialogIsShowing = true;
                    if (handler != null)
                        handler.stopScannerQRCode();//停止识别二维码
                    showQRScannerGuideDialog();
                } else {
                    showQRCodeGuideDialog();
                }
            }
        });
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(R.string.app_home_collect_money_label);
        price = getIntent().getStringExtra(Constants.ARG_PAY_PRICE);
        mQrAlipayPriceLabel.setText("￥" + price);
        mPayModel = new PayModel(this);

        parameter = new PayRequestParameter();
        parameter.merchant_id = MyApplication.getInstance().getStore().getMerchant_id() + "";
        parameter.mobile = MyApplication.getInstance().getUser().getMobile();
        parameter.price = price;
        parameter.store_id = MyApplication.getInstance().getStore().getStore_id() + "";
        parameter.trans_account = PayRequestParameter.TRANS_ACCOUNT_TYPE_WEIXIN;
//        parameter.type   类型目前又qrCode决定 具体逻辑PayModel中查看
        parameter.type = PayOrder.WEIXIN + "";
        parameter.username = MyApplication.getInstance().getUser().getUser_name();
    }

    @Override
    public void handleDecode(Result result) {
        super.handleDecode(result);
        String resultString = result.getText();
        if (resultString.equals("")) {
            showToast("Scan failed!");
            finish();
        } else {
            if (!NetworkUtils.isNetworkConnected(this)) {
                showToast(getResources().getString(R.string.no_network));
                finish();
                return;
            }
            if (validateQRCode(resultString)) {
                doCollectMoneyRequest(resultString);
            }
        }
    }

    private void doCollectMoneyRequest(String qrCode) {
        showProgress("收款中...");
        isLoading = true;
        parameter.qrCode = qrCode;
        mPayModel.collectMoneyByScanner(parameter, new Callback<PayOrderDetail>() {
            @Override
            public void onSuccess(PayOrderDetail data) {
                isLoading = false;
                closeProgress();
                goPaySuccessActivity(data);
            }

            @Override
            public void onFailure(int code, String msg) {
                isLoading = false;
                finish();
            }
        });
    }

    private void goPaySuccessActivity(PayOrderDetail data) {
        Intent intent = new Intent(this, PaySuccessActivity.class);
        intent.putExtra(Constants.ARG_PAY_ORDER_DETAIL_ENTITY, data);
        startActivity(intent);
        finish();
    }

    private boolean validateQRCode(String resultString) {
        return true;
    }

    public void showQRScannerGuideDialog() {
        WeixinQRScannerGuideDialog dialog = new WeixinQRScannerGuideDialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                stopScanner();
                startScanner();
                dialogIsShowing = false;
            }
        });
        UMEventUtil.onEvent(this, UMEvent.wechatsaofun);
        dialog.show();
    }

    public void showQRCodeGuideDialog() {
        WeixinQRCodeGuideDialog dialog = new WeixinQRCodeGuideDialog(this);
        UMEventUtil.onEvent(this, UMEvent.wechatscodefun);
        dialog.show();
    }


    @Override
    public void onTabItemClick(int currentIndex, TabView.Tab tab) {
        switch (currentIndex) {
            case 0:
                UMEventUtil.onEvent(this, UMEvent.wechatsao);
                mQrAlipayGuideHintLabel.setText(R.string.mQrScannerWeixinGuideHintLabel);
                Fragment qrCodeFragment = getSupportFragmentManager().findFragmentByTag("qrCode");
                if (qrCodeFragment != null && qrCodeFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().remove(qrCodeFragment).commit();
                }
                if (!CameraManager.get().isPreviewing() && this.currentIndex != -1) {
                    CameraManager.get().startPreview();
                    if (handler != null)
                        handler.startScannerQRCode();
                }
                mQrAlipayPriceLabel.setVisibility(View.VISIBLE);
                break;
            case 1:
                UMEventUtil.onEvent(this, UMEvent.wechatscode);
                mQrAlipayPriceLabel.setVisibility(View.INVISIBLE);
                mQrAlipayGuideHintLabel.setText("");
                parameter.type = PayRequestParameter.PAY_TYPE_WEIXIN;
                Fragment fragment = WeiXinQRCodeFragment.getInstance(parameter);
                getSupportFragmentManager().beginTransaction().add(R.id.mQrCodeContainer, fragment, "qrCode").commit();
                CameraManager.get().stopPreview();
                if (handler != null)
                    handler.stop();
                break;
            default:
                break;
        }
        this.currentIndex = currentIndex;
    }

    @Override
    public boolean isCanStartScanner() {
        return currentIndex == 0 && !dialogIsShowing && !isLoading;
    }

    @Override
    public boolean isCanStopScanner() {
        return currentIndex == 0 && !dialogIsShowing && !isLoading;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentIndex == 0) {
            UMEventUtil.onEvent(this, UMEvent.wechatsaoback);
        } else {
            UMEventUtil.onEvent(this, UMEvent.wechatscodeback);
        }
    }
}
