package com.onetoall.yjt;

import android.app.Application;

import com.onetoall.yjt.config.Environment;
import com.onetoall.yjt.domain.Store;
import com.onetoall.yjt.domain.User;
import com.onetoall.yjt.net.API;
import com.onetoall.yjt.utils.Constants;
import com.onetoall.yjt.utils.GlideDisplay;
import com.onetoall.yjt.utils.ImageDisplay;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.qw.download.DownloadConfig;
import com.qw.download.DownloadManager;
import com.qw.framework.AppStateTracker;
import com.qw.framework.utils.TextUtil;
import com.qw.framework.utils.Trace;
import com.qw.http.L;
import com.qw.http.RequestManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by qinwei on 2016/9/24 19:41
 * email:qinwei_it@163.com
 */

public class MyApplication extends Application {
    public final static String DEBUG_VERSION = "4";
    private static User user;
    private static MyApplication mInstance;
    private boolean isDev = false;
//    private boolean isDev = BuildConfig.DEBUG;
    private String token;
    private String tel;
    private Store store;
    private int storeNumber;

    public int getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(int storeNumber) {
        this.storeNumber = storeNumber;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        configApp();
    }

    private void configApp() {
        Trace.model = isDev;
        AppStateTracker.init(this);
        Environment.init(getApplicationContext());
        Environment.getInstance().loadEnvironmentConfig();
        API.setDomain(Environment.getInstance().getEnvironmentDomain());
        ImageDisplay.getInstance().init(new GlideDisplay(getApplicationContext()));
//      start init http config
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(getApplicationContext().getCacheDir(), cacheSize);
        if (isDev()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging)
//                .cache(cache)
                .connectTimeout(60, TimeUnit.SECONDS).readTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder newRequest = chain.request().newBuilder();
                        newRequest.addHeader("currentVersion", BuildConfig.VERSION_CODE + ".0");
                        newRequest.addHeader("mobileType", "0");
                        newRequest.addHeader("APIVersion", API.API_VERSION);
                        okhttp3.Response response = chain.proceed(newRequest.build());
                        return response;
                    }
                })
                .build();
        RequestManager.getInstance().init(client);
        //      end init http config

//        激光推送配置
        JPushInterface.init(this);
        JPushInterface.setDebugMode(isDev);
        DownloadManager.getInstance(getApplicationContext());
        DownloadConfig.getInstance().setDebug(isDev);
        L.DEBUG = isDev;
        Trace.model = isDev;

    }


    public static MyApplication getInstance() {
        return mInstance;
    }

    public void loginOut() {
        this.token = null;
        this.store = null;
        PrefsAccessor.getInstance(getApplicationContext()).saveString(Constants.PFA_PWD, "");
    }

    public boolean isLogin() {
        if (TextUtil.isValidate(token)) {
            return true;
        } else {
            return false;
        }
    }

    public String getToken() {
        if (token==null){
            token = PrefsAccessor.getInstance(getApplicationContext()).getString(Constants.PFA_TOKEN);
        }
        return token;
    }

    public void setToken(String token) {
        PrefsAccessor.getInstance(getApplicationContext()).saveString(Constants.PFA_TOKEN,token);
        this.token = token;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        if (user==null){
            return new User();
        }
        return user;
    }

    public String getTel() {
        return user.getMobile();
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Store getStore() {
        if (store==null){
            store = new Store();
        }
        return store;
    }

    public boolean isDev() {
        return isDev;
    }
}
