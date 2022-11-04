package com.onetoall.yjt.model;

import com.onetoall.yjt.domain.ChannelRatio;
import com.onetoall.yjt.domain.PersonnelManagementBean;

/**
 * Created by shaomengjie on 2016/11/21.
 */

public interface IPersonnelManagementModel {
    void loadPersonnelManagement(String tel,String store_number,Callback<PersonnelManagementBean> callback);
}
