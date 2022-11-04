package com.onetoall.yjt.widget.tab.v2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onetoall.yjt.R;


/**
 * Created by qinwei on 2016/10/17 11:20
 * email:qinwei_it@163.com
 */

public class TabView extends LinearLayout {
    private ImageView mTabImg;
    private TextView mTabLabel;

    public TabView(Context context) {
        super(context);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_tab_view_v2, this, true);
        mTabImg = (ImageView) findViewById(R.id.mTabImg);
        mTabLabel = (TextView) findViewById(R.id.mTabLabel);
    }

    public void initData(Tab tab) {
        mTabImg.setBackgroundResource(tab.imgResId);
        mTabLabel.setText(tab.labelResId);
        ColorStateList colorStateList = (ColorStateList) getContext().getResources().getColorStateList(tab.labelColorResId);
        if (colorStateList != null) {
            mTabLabel.setTextColor(colorStateList);//设置按钮文字颜色
        }
    }

    public void notifyDataChanged(int badgeCount) {

    }

    public static class Tab {
        public int imgResId;
        public int labelResId;
        public int labelColorResId;

        public Tab(int imgResId, int labelResId, int labelColorResId, int menuId, Class<? extends ITabFragment> mFragmentClazz) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
            this.labelColorResId = labelColorResId;
            this.menuId = menuId;
            this.mFragmentClazz = mFragmentClazz;
        }

        public int menuId;
        public Class<? extends ITabFragment> mFragmentClazz;

        public Tab(int imgResId, int labelResId) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
        }

        public Tab(int imgResId, int labelResId, Class<? extends ITabFragment> mFragmentClazz) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
            this.mFragmentClazz = mFragmentClazz;
        }

        public Tab(int imgResId, int labelResId, int labelColorResId, Class<? extends ITabFragment> mFragmentClazz) {
            this.imgResId = imgResId;
            this.labelResId = labelResId;
            this.labelColorResId = labelColorResId;
            this.mFragmentClazz = mFragmentClazz;
        }


    }
}
