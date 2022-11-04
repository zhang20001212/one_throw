package com.onetoall.yjt.model.impl;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.PersonnelManagementBean;
import com.onetoall.yjt.model.BaseModel;
import com.onetoall.yjt.model.Callback;
import com.onetoall.yjt.model.IPersonnelManagementModel;
import com.onetoall.yjt.model.OnBaseModelListener;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayCallback;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

/**
 * Created by shaomengjie on 2016/11/21.
 */

public class PersonnelManagementModel extends BaseModel implements IPersonnelManagementModel{
    public PersonnelManagementModel(OnBaseModelListener listener) {
        super(listener);
    }

    @Override
    public void loadPersonnelManagement(String tel, String store_number, final Callback<PersonnelManagementBean> callback) {
        HttpRequest request = new HttpRequest(API.loadPersonnelManagement(),HttpRequest.RequestMethod.POST);
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.put("tel",tel);
        request.put("store_number",store_number);
        request.setCallback(new EcpayCallback<PersonnelManagementBean>() {


            @Override
            public void onSuccess(PersonnelManagementBean personnelManagementBean) {
                callback.onSuccess(personnelManagementBean);
            }

            @Override
            public void onFailure(AppException e) {
                callback.onFailure(e.getCode(), e.getMsg());
            }
        });
        request.setOnGlobalExceptionListener(this);
        RequestManager.getInstance().execute("loadPersonnelManagement",request);
    }
}
