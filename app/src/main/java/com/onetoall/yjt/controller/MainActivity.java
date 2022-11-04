package com.onetoall.yjt.controller;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.onetoall.yjt.BuildConfig;
import com.onetoall.yjt.R;
import com.onetoall.yjt.controller.pay.PaySuccessActivity;
import com.onetoall.yjt.core.BaseActivity;
import com.onetoall.yjt.domain.ApkBean;
import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.domain.PushMessage;
import com.onetoall.yjt.fragment.AccountBookFragment;
import com.onetoall.yjt.fragment.MoneyCollectFragment;
import com.onetoall.yjt.fragment.ProfileFragment;
import com.onetoall.yjt.fragment.StoreFragment;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.impl.UpdateModel;
import com.onetoall.yjt.push.PushManager;
import com.onetoall.yjt.push.PushWatcher;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.onetoall.yjt.utils.UMEvent;
import com.onetoall.yjt.utils.UMEventUtil;
import com.onetoall.yjt.widget.tab.ITabFragment;
import com.onetoall.yjt.widget.tab.TabLayout;
import com.onetoall.yjt.widget.tab.TabView;
import com.qw.framework.utils.Trace;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements TabLayout.OnTabClickListener {
    private ArrayList<TabView.Tab> tabs;
    private ITabFragment mCurrentFragment;
    private int currentIndex;
    private UpdateModel mUpdateModel;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    private TabLayout mTabLayout;


    @Override
    protected void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        translucentStatus();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mUpdateModel = new UpdateModel(this);
        tabs = new ArrayList<>();
        tabs.add(new TabView.Tab(R.drawable.selector_tab_collect_money_btn, R.string.app_home_collect_money_label, R.color.selector_tab_label, R.menu.menu_collect_money, MoneyCollectFragment.class));
        tabs.add(new TabView.Tab(R.drawable.selector_tab_account_book_btn, R.string.app_home_account_book_label, R.color.selector_tab_label, AccountBookFragment.class));
        tabs.add(new TabView.Tab(R.drawable.selector_tab_shop_btn, R.string.app_home_shop_label, R.color.selector_tab_label, StoreFragment.class));
        tabs.add(new TabView.Tab(R.drawable.selector_tab_mine_btn, R.string.app_home_profile_label, R.color.selector_tab_label, ProfileFragment.class));
        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);
        checkUpdate();
    }

    private void checkUpdate() {
        mUpdateModel.loadApkInfo(BuildConfig.VERSION_CODE + "", new Callback<ApkBean>() {
            @Override
            public void onSuccess(ApkBean data) {
                if (data.getForceUpdate() != Constants.APP_UPDATE_NOT) {
                    showAppUpdateInfo(data);
                }
            }

            @Override
            public void onFailure(int code, String msg) {
            }
        });
    }

    private void showAppUpdateInfo(ApkBean data) {
        Intent intent = new Intent(this, AppUpdateActivity.class);
        intent.putExtra(Constants.ARG_APP_INFO, data);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(Constants.KEY_ACTION, -1);
        switch (action) {
            case Constants.ACTION_RESTART_APP:
                protectApp();
                break;
            case Constants.ACTION_LOGOUT:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case Constants.ACTION_BACK_TO_HOME:
//                Fragment fragment = mCurrentFragment.getFragment();
//                if (fragment instanceof MoneyCollectFragment) {
//                    MoneyCollectFragment moneyCollectFragment = (MoneyCollectFragment) fragment;
//                    moneyCollectFragment.resetClear();
//                }
                int index = intent.getIntExtra(Constants.SWITCH_FRAGMENT_INDEX, -1);
                if (index != -1) {
                    mTabLayout.setCurrentTab(index);
                    return;
                }
                MoneyCollectFragment moneyCollectFragment = (MoneyCollectFragment) getSupportFragmentManager().findFragmentByTag("0");
                if (moneyCollectFragment != null) {
                    moneyCollectFragment.resetClear();
                }
                break;
            case Constants.ACTION_EXIST_APP:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabItemClick(int index, TabView.Tab tab) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment to = fm.findFragmentByTag(index + "");
            Fragment from = fm.findFragmentByTag(currentIndex + "");
            switch (index) {
                case 0:

                    break;
                case 1:
                    UMEventUtil.onEvent(MainActivity.this, UMEvent.accountbook);
                    break;
                case 2:
                    UMEventUtil.onEvent(MainActivity.this, UMEvent.shopclick);
                    break;
                case 3:
                    UMEventUtil.onEvent(MainActivity.this, UMEvent.set);
                    break;
            }
            if (to == null) {
                to = (Fragment) tab.mFragmentClazz.newInstance();
                if (from == null) {
                    ft.add(R.id.mContainer, to, index + "").commitAllowingStateLoss();
                } else {
                    ft.hide(from).add(R.id.mContainer, to, index + "").commitAllowingStateLoss();
                }
            } else {
                ft.hide(from).show(to).commitAllowingStateLoss();
            }
            mCurrentFragment = (ITabFragment) to;
            currentIndex = index;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mCurrentFragment.onMenuItemClick(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean hasLeftIcon() {
        return false;
    }

    @Override
    protected void protectApp() {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    private PushWatcher watcher = new PushWatcher() {

        @Override
        protected void goPayResultActivity(PayOrderDetail payOrderDetail) {
            Intent intent = new Intent(MainActivity.this, PaySuccessActivity.class);
            intent.putExtra(Constants.ARG_PAY_ORDER_DETAIL_ENTITY, payOrderDetail);
            startActivity(intent);
        }

        @Override
        protected void receiverMessage(PushMessage pushMessage) {
            Trace.e("push message：" + pushMessage.toString());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushManager.getInstance().addObserver(watcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        推送未绑定则重新绑定一次
        if (!PrefsAccessor.getInstance(getApplicationContext()).getBoolean(Constants.KEY_PUSH)) {
            String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            PushManager.getInstance().setPushAlias(this, androidID);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushManager.getInstance().deleteObserver(watcher);
    }

    private long timestamp;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - timestamp < 2000) {
            super.onBackPressed();
        } else {
            showToast("再点一次退出应用哦");
            timestamp = System.currentTimeMillis();
        }
    }
}
