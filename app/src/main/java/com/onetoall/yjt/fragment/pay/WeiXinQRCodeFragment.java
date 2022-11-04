package com.onetoall.yjt.fragment.pay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseFragment;
import com.onetoall.yjt.domain.PayQRCode;
import com.onetoall.yjt.domain.PayRequestParameter;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.model.impl.PayModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.widget.tab.v2.ITabFragment;
import com.onetoall.yjt.zxing.DensityUtil;
import com.onetoall.yjt.zxing.ZxingUtil;

/**
 * Created by qinwei on 2016/10/25 15:28
 * email:qinwei_it@163.com
 */

public class WeiXinQRCodeFragment extends BaseFragment implements ITabFragment {
    private ImageView mQRCodeImg;
    private TextView mQRCodePriceLabel;
    private PayRequestParameter parameter;
    private PayModel mPayModel;
    public static Fragment getInstance(PayRequestParameter parameter) {
        WeiXinQRCodeFragment fragment = new WeiXinQRCodeFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARG_PAY_REQUEST_PARAMETER_ENTITY, parameter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.fragment_qr_code_weixin;
    }

    @Override
    protected void initView(View v) {
        mQRCodeImg = (ImageView) v.findViewById(R.id.mQRCodeImg);
        mQRCodePriceLabel = (TextView) v.findViewById(R.id.mQRCodePriceLabel);
    }

    @Override
    protected void initializeArguments(Bundle args) {
        super.initializeArguments(args);
        parameter = (PayRequestParameter) args.getSerializable(Constants.ARG_PAY_REQUEST_PARAMETER_ENTITY);
    }

    @Override
    protected void initData() {
        super.initData();
        mPayModel=new PayModel((OnBaseModelListener) getActivity());
        mQRCodePriceLabel.setText("￥ " + parameter.price);
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        mPayModel.collectMoneyByWeixinQrCode(parameter, new Callback<PayQRCode>() {
            @Override
            public void onSuccess(PayQRCode data) {
                mQRCodeImg.setImageBitmap(createImageBitmap(data.getQrcode()));
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    private Bitmap createImageBitmap(String qrCode) {
        int w = DensityUtil.dip2px(MyApplication.getInstance(), 200);
        int h = w;
        return ZxingUtil.createQRImage(qrCode, w, h);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onMenuItemClick(int id) {

    }
}
