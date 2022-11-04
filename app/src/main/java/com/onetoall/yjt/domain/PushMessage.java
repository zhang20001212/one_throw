package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * Created by qinwei on 2016/11/29 11:34
 * email:qinwei_it@163.com
 */

public class PushMessage implements Serializable{
    private String title;

    private String content;

    private String note;

    private String from;

    private String orderStatus;

    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setFrom(String from){
        this.from = from;
    }
    public String getFrom(){
        return this.from;
    }
    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }
    public String getOrderStatus(){
        return this.orderStatus;
    }
    public void setNote(String note){
        this.note = note;
    }
    public String getNote(){
        return this.note;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
