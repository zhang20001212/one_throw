package com.onetoall.yjt.core;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.MainActivity;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.ResponseCode;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.widget.QToolbar;
import com.qw.framework.AppStateTracker;
import com.qw.framework.utils.Trace;
import com.qw.http.AppException;
import com.umeng.analytics.MobclickAgent;

/**
 * 解決应用强杀导致的null指针错误
 * Created by qinwei on 2016/9/24 21:17
 * email:qinwei_it@163.com
 */

public abstract class BaseActivity extends AppCompatActivity implements OnBaseModelListener {
    public QToolbar mToolbar;
    private int mMenuId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppStateTracker.getInstance().getAppState() != AppStateTracker.APP_STATE_INIT_OR_KILLED) {
            setContentView();
            initToolbar();
            initView();
            initData(savedInstanceState);
        } else {
            protectApp();
        }
    }


    /**
     * 设置视图
     */
    protected abstract void setContentView();

    /**
     * 初始化toolbar
     */
    protected void initToolbar() {
        mToolbar = (QToolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            translucentStatus();
            setSupportActionBar(mToolbar);
            if (hasLeftIcon())
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (titleCenter()) {
                mToolbar.setTextGravity(QToolbar.GRAVITY_CENTER);
            } else {
                mToolbar.setTextGravity(QToolbar.GRAVITY_LEFT);
            }
            mToolbar.setTitle(getSupportActionBar().getTitle().toString());
        }
    }

    protected void translucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 处理数据
     *
     * @param savedInstanceState 数据状态恢复
     */
    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        this.setContentView(layoutResID, false);
    }

    public void setContentView(@LayoutRes int layoutResID, boolean hasTitle) {
        if (hasTitle) {
            ViewGroup root = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.base_layout_has_title, null);
            LayoutInflater.from(this).inflate(layoutResID, root);
            super.setContentView(root);
        } else {
            super.setContentView(layoutResID);
        }
    }

    /**
     * 应用被强杀重启app
     */
    protected void protectApp() {
        Trace.d("protectApp " + getClass().getSimpleName() + " killed will do reStart");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.KEY_ACTION, Constants.ACTION_RESTART_APP);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected void onStart() {
        if (AppStateTracker.getInstance().isShowGesture()) {
            Trace.d("show gesture activity");
            AppStateTracker.getInstance().setScreenOff(false);
        }
        super.onStart();

    }

    @Override
    public final boolean onCreateOptionsMenu(Menu menu) {
        if (mMenuId != 0) {
            getMenuInflater().inflate(mMenuId, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 重写父类的setTitle方法根据当前标题显示是否居中做相应处理
     *
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        if (mToolbar != null) mToolbar.setTitle(title);
    }

    @Override
    protected final void onTitleChanged(CharSequence title, int color) {
    }

    public void setMenuId(int menuId) {
        int tempMenuId = mMenuId;
        this.mMenuId = menuId;
        if (tempMenuId != mMenuId) {
//TODO refresh menu
            invalidateOptionsMenu();
        }
    }

    protected boolean titleCenter() {
        return true;
    }

    protected boolean hasLeftIcon() {
        return true;
    }

    public void finishActivity() {
        finish();
    }

    ProgressDialog progressDialog;

    public void showProgress() {
        showProgress(null);
    }

    public void showProgress(String msg) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, null, msg);
        }
    }


    public void showProgress(String tag, String msg) {

    }

    public void closeProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public boolean onGlobalExceptionHandler(AppException e) {
        e.printStackTrace();
        closeProgress();
//        e.getCode()
//        TODO token valid
        if (e.getType() == AppException.ErrorType.SERVER) {
            switch (e.getCode()) {
                case 404:
                    Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "url not found", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
//                    Toast.makeText(this, "server error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                    break;
                case ResponseCode.CODE_TOKEN_NOT_VALIDATE://token 失效
                    showGoLoginDialog();
                    break;
                case ResponseCode.CODE_SERVER_UPDATE:
                    Toast.makeText(this, e.getMsg(), Toast.LENGTH_SHORT).show();
                    break;
                case ResponseCode.CODE_ORDER_CANCEL:
                default:
                    Toast.makeText(this, e.getMsg(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (e.getType() == AppException.ErrorType.IO || e.getType() == AppException.ErrorType.TIMEOUT) {
            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
        } else if (e.getType() == AppException.ErrorType.JSON) {
            Toast.makeText(this, getString(R.string.error_json), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void showGoLoginDialog() {
        new AlertDialog.Builder(this).setNegativeButton(R.string.exist, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                intent.putExtra(Constants.KEY_ACTION, Constants.ACTION_EXIST_APP);
                startActivity(intent);
            }
        }).setPositiveButton(R.string.Sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                intent.putExtra(Constants.KEY_ACTION, Constants.ACTION_LOGOUT);
                startActivity(intent);
            }
        }).setMessage("登陆超时，为了您账户安全起见，重新验证身份。").setCancelable(false).show();
    }

    protected void showLongToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }
}
