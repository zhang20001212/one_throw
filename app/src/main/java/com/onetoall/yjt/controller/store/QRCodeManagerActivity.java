package com.onetoall.yjt.controller.store;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.core.BaseListActivity;
import com.onetoall.yjt.domain.QRCodeInfo;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.QRCodeModel;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.line.DividerItemDecoration;
import com.qw.framework.utils.Trace;
import com.qw.framework.widget.pull.BaseViewHolder;
import com.qw.framework.widget.pull.PullRecyclerView;
import com.qw.http.RequestManager;

import java.util.ArrayList;

/**
 * Created by qinwei on 2016/11/8 10:17
 * email:qinwei_it@163.com
 */

public class QRCodeManagerActivity extends BaseListActivity<QRCodeInfo> implements View.OnClickListener {

    private static final int REQUEST_CODE_ADD = 100;
    private QRCodeModel mQrCodeModel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_qr_code_manager, true);
    }

    @Override
    protected BaseViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(this).inflate(R.layout.layout_qr_code_manager_item, parent, false));
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.mQRCodeManagerAddBtn).setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle(getString(R.string.title_qr_code_manager));
        mPullRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mPullRecyclerView.setEnablePullToEnd(false);
        mQrCodeModel = new QRCodeModel(this);
        loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
        if(MyApplication.getInstance().getStore().isShopkeeper()){
            findViewById(R.id.mQRCodeManagerAddBtn).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.mQRCodeManagerAddBtn).setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh(int mode) {
        super.onRefresh(mode);
        loadDataFromServer(mode);
    }

    private void loadDataFromServer(final int mode) {
        mQrCodeModel.loadQRCodes(MyApplication.getInstance().getStore().getStore_id() + "", new Callback<ArrayList<QRCodeInfo>>() {
            @Override
            public void onSuccess(ArrayList<QRCodeInfo> data) {
                if (mode == PullRecyclerView.MODE_PULL_TO_START) {
                    modules.clear();
                }
                modules.addAll(data);
                adapter.notifyDataSetChanged();
                mPullRecyclerView.onRefreshCompleted(mode);
                if (data.size() == 0) {
                    showQrAddDialog();
                }
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    private void showQrAddDialog() {
        new AlertDialog.Builder(this).setTitle("溫馨提示")
                .setCancelable(false)
                .setMessage("您未绑定什码付,是否添加什码付？")
                .setNegativeButton(R.string.Sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goQRCodeScanner();
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消指定tag的请求 防止异步线程导致内存泄漏
        RequestManager.getInstance().cancel(QRCodeModel.LOAD_QR_CODES);
    }

    @Override
    public void onClick(View v) {
        goQRCodeScanner();
    }

    public void goQRCodeScanner() {
        UMEventUtil.onEvent(this, UMEvent.shoppayaddnew);
        Intent intent = new Intent(this, QRCodeScannerActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD) {
                final String qrCode = data.getStringExtra(Constants.ARG_QR_CODE_INFO);
                new AlertDialog.Builder(this).setTitle("绑定二维码")
                        .setCancelable(false)
                        .setMessage("确认添加二维码吗？")
                        .setNegativeButton(R.string.Sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bindQrCodeRequest(qrCode);
                            }
                        })
                        .setPositiveButton(R.string.cancel, null).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bindQrCodeRequest(String qrCode) {
        Trace.d(qrCode);
        showProgress();
        mQrCodeModel.bindQrCode(MyApplication.getInstance().getStore().getStore_id() + "", qrCode, new Callback<Result>() {
            @Override
            public void onSuccess(Result data) {
                showToast(data.getMsg());
                closeProgress();
                loadDataFromServer(PullRecyclerView.MODE_PULL_TO_START);
            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    class Holder extends BaseViewHolder implements View.OnClickListener {
        private TextView mQRCodeItemTitleLabel;
        private TextView mQRCodeItemMsgLabel;
        private QRCodeInfo qr;

        public Holder(View itemView) {
            super(itemView);
            mQRCodeItemTitleLabel = (TextView) itemView.findViewById(R.id.mQRCodeItemTitleLabel);
            mQRCodeItemMsgLabel = (TextView) itemView.findViewById(R.id.mQRCodeItemMsgLabel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(int i) {
            qr = modules.get(i);
            mQRCodeItemMsgLabel.setText(qr.getBinding_time());
            mQRCodeItemTitleLabel.setText(qr.getQrcode_id());
        }

        @Override
        public void onClick(View v) {
            UMEventUtil.onEvent(getBaseContext(), UMEvent.shoppaysmfno);
            Intent in = new Intent(QRCodeManagerActivity.this, QRCodeDetailActivity.class);
            in.putExtra(Constants.ARG_QR_CODE_INFO, qr);
            startActivity(in);
        }
    }


}
