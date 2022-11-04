package com.onetoall.yjt.core;

import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.onetoall.yjt.R;
import com.onetoall.yjt.widget.RefreshView;


/**
 * 耗时任务状态加载基类(数据加载状态切换，下拉刷新)
 * Created by qinwei on 2016/11/16 13:20
 * email:qinwei_it@163.com
 */

public abstract class BaseRefreshActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshView.OnRetryListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RefreshView mRefreshView;

    @Override
    public void setContentView(@LayoutRes int layoutResID, boolean hasTitle) {
        if (hasTitle) {
            ViewGroup root = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.base_layout_has_title_and_refresh_view, null);
            ViewGroup content = (ViewGroup) root.findViewById(R.id.mRefreshView);
            LayoutInflater.from(this).inflate(layoutResID, content);
            super.setContentView(root);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        mRefreshView = (RefreshView) findViewById(R.id.mRefreshView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(canPullToRefresh());
        mRefreshView.setOnRetryListener(this);
        setPullToRefreshEnable(false);
        changeRefreshState(RefreshView.State.ing);
    }

    public void setPullToRefreshEnable(boolean enable) {
        if (canPullToRefresh()) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    protected void changeRefreshState(RefreshView.State state) {
        switch (state) {
            case ing:
                setPullToRefreshEnable(false);
                break;
            case done:
                setPullToRefreshEnable(true);
                break;
            case empty:
                setPullToRefreshEnable(true);
                break;
            case error:
                setPullToRefreshEnable(true);
                break;
            default:
                break;
        }
        mRefreshView.notifyDataChanged(state);
    }

    @Override
    public void onRefresh() {

    }

    protected void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onRetry() {

    }

    /**
     * 控制是否支持下拉刷新功能
     *
     * @return true支持下拉刷新 false 反之
     */
    public boolean canPullToRefresh() {
        return false;
    }

}
