package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.MessageBean;

import java.util.ArrayList;

/**
 * Created by shaomengjie on 2016/12/6.
 */

public interface IMessageModel {
    void loadMessage(String id, String username, Callback<ArrayList<MessageBean>> callback);
    void commitUpdata(String id);
}
