package com.onetoall.yjt.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.onetoall.yjt.MyApplication;
import com.qw.http.L;
import com.qw.http.RequestManager;
import com.umeng.analytics.MobclickAgent;


/**
 * fragment简单的封装
 *
 * @author 秦伟
 * @version 1.0
 * @created 创建时间: 2015-8-22 下午7:07:47
 */
public abstract class BaseFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    public static final String KEY_IS_BIND_VIEWPAGER = "key_is_bind_viewpager";
    private boolean isFirstLoad = true;
    private boolean isOnViewCreated = false;
    private boolean isBindViewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        trace("onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstLoad = true;
        trace("onCreate " + (savedInstanceState == null));
        if (getArguments() != null)
            initializeArguments(getArguments());
    }

    /**
     * 初始化fragment的参数配置
     *
     * @param args 配置参数
     */
    protected void initializeArguments(Bundle args) {
        if (args != null) {
            isBindViewPager = args.getBoolean(KEY_IS_BIND_VIEWPAGER, false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        trace("onStart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trace("onCreateView");
        return inflater.inflate(getFragmentLayoutId(), container, false);
    }

    protected abstract int getFragmentLayoutId();

    protected abstract void initView(View v);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trace("onViewCreated");
        isOnViewCreated = true;
        initView(view);
        if (!isBindViewPager) {
            initData();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        trace("setUserVisibleHint " + isVisibleToUser);
        if (isFirstLoad && isVisibleToUser && isOnViewCreated) {
            trace("lazyLoad start call initData()");
            initData();
            isFirstLoad = false;
        }
    }

    protected void initData() {
        trace("initData");
    }

    @Override
    public void onResume() {
        super.onResume();
        trace("onResume");
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        trace("onPause");
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        trace("onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        trace("onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        trace("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        trace("onDestroy");
        RequestManager.getInstance().cancelAll();
    }

    private void trace(String msg) {
//        Trace.d(TAG+":"+msg);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        L.d(getClass().getSimpleName() + " hidden:" + hidden);
    }

    protected void showLongToast(String msg) {
        Toast.makeText(MyApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(MyApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
