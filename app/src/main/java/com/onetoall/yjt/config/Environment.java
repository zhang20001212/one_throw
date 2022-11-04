package com.onetoall.yjt.config;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;
import android.widget.Toast;

import com.onetoall.yjt.R;
import com.onetoall.yjt.utils.PrefsAccessor;
import com.qw.framework.utils.TextUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qinwei on 2016/10/20 4:19
 * email:qinwei_it@163.com
 */
public class Environment {
    public static final String KEY_ENVIRONMENT_TAG = "key_environment_tag";
    public static final String KEY_ENVIRONMENT_CUSTOM_DOMAIN = "key_environment_custom_domain";
    public static final String TAG_DEV = "DEV";
    public static final String TAG__PRE = "PRE";
    public static final String TAG__PRD = "PRD";
    public static final String TAG_SERVER_URL = "ServerURL";
    public static final String TAG_IMAGE_URL = "ImageURL";
    private static Environment mEnvironment;
    private Context mContext;
    private HashMap<String, HashMap<String, String>> environmentConfig;
    private ArrayList<EnvironmentBean> environmentBeans;
    private String defaultTag;

    private Environment(Context context) {
        this.mContext = context.getApplicationContext();
        this.environmentBeans = new ArrayList<>();
    }

    public static void init(Context context) {
        if (mEnvironment == null) {
            mEnvironment = new Environment(context);
            if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                Toast.makeText(context, "No SDCard!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public static Environment getInstance() {
        if (mEnvironment == null) {
            throw new RuntimeException("you must call init()");
        }
        return mEnvironment;
    }

    public void loadEnvironmentConfig() {
        InputStream is = mContext.getResources().openRawResource(R.raw.environment_config);
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            HashMap<String, String> map = null;
            boolean serverUrlFlag = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        environmentConfig = new HashMap<>();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Environment")) {
                            defaultTag = parser.nextText();
                        }
                        if (name.equalsIgnoreCase(TAG_SERVER_URL)) {
                            map = new HashMap<>();
                            serverUrlFlag = true;
                        } else if (name.equalsIgnoreCase(TAG_IMAGE_URL)) {
                            map = new HashMap<>();
                            serverUrlFlag = false;
                        } else if (map != null) {

                            EnvironmentBean bean = new EnvironmentBean();
                            if (name.equalsIgnoreCase(TAG_DEV)) {
                                bean.name = TAG_DEV;
                                bean.domain = parser.nextText();
                                map.put(bean.name, bean.domain);
                                if (serverUrlFlag) environmentBeans.add(bean);
                            } else if (name.equalsIgnoreCase(TAG__PRE)) {
                                bean.name = TAG__PRE;
                                bean.domain = parser.nextText();
                                map.put(bean.name, bean.domain);
                                if (serverUrlFlag) environmentBeans.add(bean);
                            } else if (name.equalsIgnoreCase(TAG__PRD)) {
                                bean.name = TAG__PRD;
                                bean.domain = parser.nextText();
                                map.put(bean.name, bean.domain);
                                if (serverUrlFlag) environmentBeans.add(bean);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase(TAG_SERVER_URL) && map != null) {
                            environmentConfig.put(TAG_SERVER_URL, map);
                            map = null;
                        } else if (parser.getName().equalsIgnoreCase(TAG_IMAGE_URL) && map != null) {
                            environmentConfig.put(TAG_IMAGE_URL, map);
                            map = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<EnvironmentBean> getEnvironmentBeans() {
        return environmentBeans;
    }

    public String getEnvironmentDomain() {
//        TODO 读取缓存的切换环境
        String environmentTag = PrefsAccessor.getInstance(mContext).getString(KEY_ENVIRONMENT_TAG);
//        TODO 没有缓存环境 取xml的默认环境
        if (!TextUtil.isValidate(environmentTag)) {
            environmentTag = defaultTag;
        }
//        TODO 获取环境url
        String url = "";
        if (environmentTag.equals(TAG_DEV)) {//如果环境为dev 先取自定义domain
            url = PrefsAccessor.getInstance(mContext).getString(KEY_ENVIRONMENT_CUSTOM_DOMAIN);
        }
        if (TextUtils.isEmpty(url)) {//自定义domain未设置则去xml环境配置domain
            url = environmentConfig.get(TAG_SERVER_URL).get(environmentTag);
        }
        return url;
    }

    public String getEnvironmentName() {
        String tag = PrefsAccessor.getInstance(mContext).getString(Environment.KEY_ENVIRONMENT_TAG);
//        TODO 没有缓存名称 取xml默认环境名称
        if (!TextUtil.isValidate(tag)) {
            tag = defaultTag;
        }
        return tag;
    }

    public static class EnvironmentBean {
        public String name;
        public String domain;
    }
}
