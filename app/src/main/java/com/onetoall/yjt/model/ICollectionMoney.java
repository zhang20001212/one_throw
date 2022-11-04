package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.CollectionMoneyBean;

/**
 * Created by shaomengjie on 2016/11/20.
 */

public interface ICollectionMoney {
    void loadCollectionMoney(String store_id,String rows,Callback<CollectionMoneyBean> callback);
}
