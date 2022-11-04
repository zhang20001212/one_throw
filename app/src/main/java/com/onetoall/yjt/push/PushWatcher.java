package com.onetoall.yjt.push;

import com.onetoall.yjt.domain.PayOrderDetail;
import com.onetoall.yjt.domain.PushMessage;
import com.qw.http.JsonParser;

import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by qinwei on 2016/11/15 10:49
 * email:qinwei_it@163.com
 */

public abstract class PushWatcher implements Observer {
    @Override
    public void update(Observable observable, Object data) {
        if (data != null && data instanceof String) {
            String msg = (String) data;
            try {
                JSONObject jsonObject=new JSONObject(msg);
                String orderStatus=jsonObject.getString("orderStatus");
                if(orderStatus.equals("10")){
                    receiverMessage(JsonParser.deserializeFromJson(msg, PushMessage.class));
                }else{
                    PayOrderDetail payOrderDetail = JsonParser.deserializeFromJson(msg, PayOrderDetail.class);
                    goPayResultActivity(payOrderDetail);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected  void receiverMessage(PushMessage pushMessage){};

    protected  void goPayResultActivity(PayOrderDetail payOrderDetail){};
}
