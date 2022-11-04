package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.QRCodeInfo;
import com.onetoall.yjt.domain.Result;

import java.util.ArrayList;

/**
 * 二维码相关model
 * Created by qinwei on 2016/11/4 16:42
 * email:qinwei_it@163.com
 */

public interface IQRCodeModel {
    /**
     * 获取什码付列表
     *
     * @param storeId
     * @param callback
     */
    void loadQRCodes(String storeId, Callback<ArrayList<QRCodeInfo>> callback);

    /**
     * 绑定什码付
     * @param store_id
     * @param qrcode_id
     * @param callBack
     */
    void bindQrCode(String store_id, String qrcode_id, Callback<Result> callBack);
}
