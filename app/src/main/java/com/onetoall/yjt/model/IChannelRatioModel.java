package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.ChannelRatio;

/**
 * Created by shaomengjie on 2016/11/18.
 */

public interface IChannelRatioModel {
    void loadChanneRatio(String store_id,Callback<ChannelRatio> callback);
}
