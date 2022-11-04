package com.onetoall.yjt.net;

import com.google.gson.JsonSyntaxException;
import com.qw.http.AbstractCallback;
import com.qw.http.AppException;
import com.qw.http.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by qinwei on 2016/10/22 16:44
 * email:qinwei_it@163.com
 */

public abstract class Ecpay2001Callback<T> extends AbstractCallback<T> {
    @Override
    public T convert(String content) throws AppException {
        try {
            Type type = getClass().getGenericSuperclass();
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            JSONObject jsonObject = new JSONObject(content);
            int code = jsonObject.getInt("code");
            if (code == ResponseCode.CODE_SUCCESS || code == ResponseCode.CODE_ORDER_CANCEL) {
                String data = jsonObject.opt("data").toString();
                return JsonParser.deserializeFromJson(data, type);
            } else {
                throw new AppException(code, jsonObject.getString("msg"));
            }
        } catch (JsonSyntaxException e) {
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        } catch (JSONException e) {
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        } catch (Exception e){
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        }
    }
}
