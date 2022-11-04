package com.onetoall.yjt.controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.domain.ApkBean;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.PermissionHelper;
import com.qw.download.DownloadDBController;
import com.qw.download.DownloadEntity;
import com.qw.download.DownloadFileUtil;
import com.qw.download.DownloadManager;
import com.qw.download.DownloadWatcher;
import com.qw.framework.utils.Trace;

import java.io.File;

/**
 * Created by qinwei on 2016/11/23 11:40
 * email:qinwei_it@163.com
 */
public class AppUpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mAppUpdateTitleLabel;
    private TextView mAppUpdateContentLabel;
    private ProgressBar progressBar;
    private TextView mAppUpdateProgressInfoLabel;
    private Button mAppUpdateLeftBtn;
    private Button mAppUpdateRightBtn;
    private ApkBean mAppInfo;
    private DownloadEntity e;
    private DownloadWatcher watcher = new DownloadWatcher() {
        @Override
        protected void onDataChanged(DownloadEntity downloadEntity) {
            AppUpdateActivity.this.e = downloadEntity;
            showButtonText();
            setUpdateInfo(downloadEntity);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_app_update);
        DownloadManager.getInstance(getApplicationContext()).addObserver(watcher);
        mAppUpdateTitleLabel = (TextView) findViewById(R.id.mAppUpdateTitleLabel);
        mAppUpdateContentLabel = (TextView) findViewById(R.id.mAppUpdateContentLabel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAppUpdateProgressInfoLabel = (TextView) findViewById(R.id.mAppUpdateProgressInfoLabel);
        mAppUpdateLeftBtn = (Button) findViewById(R.id.mAppUpdateLeftBtn);
        mAppUpdateRightBtn = (Button) findViewById(R.id.mAppUpdateRightBtn);
        mAppUpdateLeftBtn.setOnClickListener(this);
        mAppUpdateRightBtn.setOnClickListener(this);
        hiddenProgressInfo();
        initData();
    }

    private void initData() {
        mAppInfo = (ApkBean) getIntent().getSerializableExtra(Constants.ARG_APP_INFO);
        mAppUpdateContentLabel.setText(mAppInfo.getMsg());
        mAppUpdateTitleLabel.setText("当前最新版本为 v" + mAppInfo.getVersion());
        e = DownloadManager.getInstance(getApplicationContext()).findById(getDownloadId(mAppInfo.getUrl()));
        if (e == null) {
            e = new DownloadEntity(getDownloadId(mAppInfo.getUrl()), mAppInfo.getUrl());
        }
        showButtonText();
        if (e.state == DownloadEntity.State.idle) {
            hiddenProgressInfo();
        } else {
            int p = (int) (100.0 * e.currentLength / e.contentLength);
            progressBar.setProgress(p);
            mAppUpdateProgressInfoLabel.setText(e.currentLength + "/" + e.contentLength);
            showProgressInfo();
        }
        if (mAppInfo.getForceUpdate() == Constants.APP_UPDATE_FORCE) {
            mAppUpdateLeftBtn.setVisibility(View.GONE);
        } else {
            mAppUpdateLeftBtn.setText("下次再说");
        }
    }

    private void showButtonText() {
        switch (e.state) {
            case paused:
                mAppUpdateRightBtn.setText("继续");
                break;
            case wait:
                mAppUpdateRightBtn.setText("等待");
                break;
            case ing:
            case connect:
                mAppUpdateRightBtn.setText("下载中");
                break;
            case error:
                mAppUpdateRightBtn.setText("重试");
                break;
            case cancelled:
            case idle:
                mAppUpdateRightBtn.setText("更新");
                break;
            case done:
//                FIXME 用户误删apk包的处理
                File file = new File(DownloadFileUtil.getDownloadPath(e.id));
                if (file.exists()) {
                    mAppUpdateRightBtn.setText("安裝");
                } else {
                    DownloadDBController.getInstance(getApplicationContext()).delete(e);
                    e = new DownloadEntity(getDownloadId(mAppInfo.getUrl()), mAppInfo.getUrl());
                }

                break;
            default:
                break;
        }
    }

    private String getDownloadId(String url) {
        int index = url.lastIndexOf("/");
        String after = url.substring(index + 1);
        return after;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mAppUpdateLeftBtn:
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.mAppUpdateRightBtn:
                if (PermissionHelper.requestPermission(this, PermissionHelper.Permission.STORAGE, PermissionHelper.ASK_WRITE_EXTERNAL_STORAGE)) {
                    handlerDownload();
                }
                break;
            default:
                break;
        }
    }

    private void handlerDownload() {
        switch (e.state) {
            case ing:
            case wait:
            case connect:
                DownloadManager.getInstance(MyApplication.getInstance()).pauseDownload(e);
                break;
            case paused:
                DownloadManager.getInstance(MyApplication.getInstance()).resumeDownload(e);
                break;
            case error:
                DownloadManager.getInstance(MyApplication.getInstance()).resumeDownload(e);
                break;
            case idle:
                showProgressInfo();
                DownloadManager.getInstance(MyApplication.getInstance()).addDownload(e);
                break;
            case done:
                String path = DownloadFileUtil.getDownloadPath(e.id);
                installApp(path);
                break;
            default:
                break;
        }
    }

    private void hiddenProgressInfo() {
        progressBar.setVisibility(View.GONE);
        mAppUpdateProgressInfoLabel.setVisibility(View.GONE);
    }


    private void showProgressInfo() {
        progressBar.setVisibility(View.VISIBLE);
        mAppUpdateProgressInfoLabel.setVisibility(View.VISIBLE);
    }

    public void setUpdateInfo(DownloadEntity e) {
        Trace.e("setUpdateInfo state:" + e.state.name());
        switch (e.state) {
            case ing:
                int p = (int) (100.0 * e.currentLength / e.contentLength);
                progressBar.setProgress(p);
                mAppUpdateProgressInfoLabel.setText(e.currentLength + "/" + e.contentLength);
                break;
            case done:
                progressBar.setProgress(100);
                mAppUpdateProgressInfoLabel.setText(e.contentLength + "/" + e.contentLength);
                String path = DownloadFileUtil.getDownloadPath(e.id);
                installApp(path);
                break;
            default:
                break;
        }
    }

    private void installApp(String path) {
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            Toast.makeText(this, "apk 安装包不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHelper.ASK_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "获取读写sd卡权限已被禁止,无法完成更新操作", Toast.LENGTH_SHORT).show();
            } else {
                handlerDownload();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance(getApplicationContext()).removeObserver(watcher);
    }

    @Override
    public void onBackPressed() {
    }
}
