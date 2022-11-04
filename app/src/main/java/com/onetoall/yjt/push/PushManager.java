package com.onetoall.yjt.push;

import android.content.Context;

import com.onetoall.yjt.MyApplication;
import com.onetoall.yjt.domain.Result;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.net.EcpayResultCallBack;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.qw.framework.utils.Trace;
import com.qw.http.AppException;
import com.qw.http.HttpRequest;
import com.qw.http.RequestManager;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by qinwei on 2016/11/15 10:48
 * email:qinwei_it@163.com
 */

public class PushManager {
    public static PushManager mInstance;

    private PushManager() {
    }

    public static PushManager getInstance() {
        if (mInstance == null) {
            mInstance = new PushManager();
        }
        return mInstance;
    }

    public void addObserver(PushWatcher watcher) {
        PushChanger.getInstance().addObserver(watcher);
    }

    public void deleteObserver(PushWatcher watcher) {
        PushChanger.getInstance().deleteObserver(watcher);
    }

    public void deleteObservers() {
        PushChanger.getInstance().deleteObservers();
    }


    public void setPushAlias(Context context, final String androidID){
        JPushInterface.setAlias(context, androidID+"", new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                Trace.e("setAlias result msg:"+code+"  "+s+", set:");
                String logs="";
                switch (code) {
                    case 0:
                        logs = "Set tag and alias success";
                        bindPushID(androidID);
                        Trace.e(logs);
                        break;
                    case 6002:
                        logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                        Trace.e(logs);
                        break;
                    default:
                        logs = "Failed with errorCode = " + code;
                        Trace.e(logs);
                }
            }
        });
    }
    private void bindPushID(String androidID){
        HttpRequest request=new HttpRequest(API.loadPushId(), HttpRequest.RequestMethod.POST);
        request.put("store_id", MyApplication.getInstance().getStore().getStore_id() + "");
        request.put("token", androidID);
        request.put("tel", MyApplication.getInstance().getTel());
        request.addHeader("X-Security-Token", MyApplication.getInstance().getToken());
        request.setCallback(new EcpayResultCallBack<Result>() {
            @Override
            public void onSuccess(Result s) {
                Trace.e("bind push id result msg:"+s.toString());
                PrefsAccessor.getInstance(MyApplication.getInstance()).saveBoolean(Constants.KEY_PUSH,true);
            }

            @Override
            public void onFailure(AppException e) {
                e.printStackTrace();
            }
        });
        RequestManager.getInstance().execute("",request);
    }
}
