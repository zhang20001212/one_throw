package com.onetoall.yjt.widget.tab.v2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.qw.framework.utils.TextUtil;

import java.util.ArrayList;

/**
 * 扫码收款 工具栏容器
 * Created by qinwei on 2016/10/17 11:30
 * email:qinwei_it@163.com
 */

public class TabLayout extends LinearLayout implements View.OnClickListener {
    public static final int ID_PREFIX = 1000;
    private OnTabClickListener listener;
    private ArrayList<TabView.Tab> tabs;
    private int currentIndex = -1;

    public TabLayout(Context context) {
        super(context);
        initView(context);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);

    }

    public void initData(ArrayList<TabView.Tab> tabs, OnTabClickListener listener) {
        this.listener = listener;
        this.tabs = tabs;
        if (!TextUtil.isValidate(tabs)) {
            throw new IllegalArgumentException("tabs can't be null");
        }
        TabView tabView = null;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        for (int i = 0; i < tabs.size(); i++) {
            tabView = new TabView(getContext());
            tabView.setId(i + ID_PREFIX);
            tabView.initData(tabs.get(i));
            tabView.setOnClickListener(this);
            addView(tabView, layoutParams);
        }
    }

    public void notifyDataChanged(int index, int badgeCount) {
        ((TabView) findViewById(index + ID_PREFIX)).notifyDataChanged(badgeCount);
    }

    public void setCurrentTab(int index) {
        if (index != currentIndex) {
            onClick(findViewById(index + ID_PREFIX));
        }
    }

    @Override
    public void onClick(View v) {
        int index = v.getId() - ID_PREFIX;
        if (index != currentIndex) {
            View from = findViewById(currentIndex + ID_PREFIX);
            View to = findViewById(index + ID_PREFIX);
            if (from != null) {
                from.setSelected(false);
            }
            to.setSelected(true);
            currentIndex = index;
            listener.onTabItemClick(index, tabs.get(v.getId() - ID_PREFIX));
        }
    }

    public interface OnTabClickListener {
        void onTabItemClick(int currentIndex, TabView.Tab tab);
    }
}
