package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.ApkBean;

/**
 * Created by user on 2016/11/22.
 */

public interface IUpdateModel {
    /**
     *
     * @param callback
     */
    void loadApkInfo(String versonCode, Callback<ApkBean> callback);
}
