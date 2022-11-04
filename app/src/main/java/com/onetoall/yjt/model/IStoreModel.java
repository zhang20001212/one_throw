package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.Store;

/**
 * Created by qinwei on 2016/10/23 15:39
 * email:qinwei_it@163.com
 */

public interface IStoreModel {
    /**
     * 查询商品详情信息
     *
     * @param storeId
     * @param callback
     */
    void loadStoreInfo(String storeId, String tel, String storeNumber, Callback<Store> callback);
}
