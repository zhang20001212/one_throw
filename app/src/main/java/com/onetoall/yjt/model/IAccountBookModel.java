package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.AccountBookIndex;
import com.onetoall.yjt.domain.PayOrderDetail;

/**
 * Created by qinwei on 2016/10/22 18:44
 * email:qinwei_it@163.com
 */

public interface IAccountBookModel {
    /**
     * @param store_id 门店id
     * @param dataLong 时间戳
     * @param callback 异步回调
     */
    void loadAccountBookIndex(String store_id, String dataLong, Callback<AccountBookIndex> callback);


    void loadPayOrderById(String orderId, Callback<PayOrderDetail> callback);

}
