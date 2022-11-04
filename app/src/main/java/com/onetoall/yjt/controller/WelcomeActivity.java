package com.onetoall.yjt.controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.onetoall.yjt.BuildConfig;
import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.R;
import com.onetoall.yjt.config.Environment;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.PermissionHelper;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.qw.framework.AppStateTracker;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动界面
 *
 * @author qinwei
 * @since 2016/9/24
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final int LOGIN = 1;//已经登录
    private static final int NO_LOGIN = 2;//未登陆
    private static final int AD = 3;//显示广告
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = null;
            switch (msg.what) {
                case LOGIN:
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    break;
                case NO_LOGIN:
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    break;
                default:
                    break;
            }
            startActivity(intent);
            finish();
        }
    };
    private TextView mVersionNameLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PrefsAccessor.getInstance(MyApplication.getInstance()).saveBoolean(Constants.KEY_PUSH, false);//设备唯一标识未bind server
        AppStateTracker.getInstance().setAppState(AppStateTracker.APP_STATE_OFFLINE);
        setContentView(R.layout.activity_welcome);
        mVersionNameLabel = (TextView) findViewById(R.id.mVersionNameLabel);
        if (!MyApplication.getInstance().isDev()) {
            mVersionNameLabel.setText("v" + BuildConfig.VERSION_NAME);
        } else {
            mVersionNameLabel.setText("v" + BuildConfig.VERSION_NAME + " " + Environment.getInstance().getEnvironmentName() + MyApplication.DEBUG_VERSION);
        }
        if (PermissionHelper.requestPermission(this, PermissionHelper.Permission.READ_PHONE_STATE, PermissionHelper.ASK_READ_PHONE_STATE)) {
            mHandler.sendEmptyMessageDelayed(LOGIN, 2000);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(LOGIN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHelper.ASK_READ_PHONE_STATE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "获取手机状态权限已被禁止", Toast.LENGTH_SHORT).show();
            }
            mHandler.sendEmptyMessageDelayed(LOGIN, 1000);
        }
    }
}
