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
 * Created by user on 2016/11/15.
 */

public abstract class EcpayResultCallBack<T> extends AbstractCallback<T> {
    @Override
    public T convert(String content) throws AppException {
        try {
            Type type = getClass().getGenericSuperclass();
            type = ((ParameterizedType) type).getActualTypeArguments()[0];
            JSONObject jsonObject = new JSONObject(content);
            int code = jsonObject.getInt("code");
            if (code == 0) {
                return JsonParser.deserializeFromJson(content, type);
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
