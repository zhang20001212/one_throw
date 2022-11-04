package com.onetoall.yjt.widget.tab.v2;

import android.support.v4.app.Fragment;

/**
 * Created by qinwei on 2016/10/17 11:35
 * email:qinwei_it@163.com
 */

public interface ITabFragment {
    Fragment getFragment();

    void onMenuItemClick(int id);
}
