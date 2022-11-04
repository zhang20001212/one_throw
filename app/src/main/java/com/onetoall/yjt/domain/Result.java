package com.onetoall.yjt.domain;

import java.io.Serializable;

/**
 * server 返回結果数据模型
 * Created by user on 2016/11/15.
 */

public class Result implements Serializable {
    /**
     * msg :
     * code : 0
     * data : {"dsa":"asd"}
     * status : success
     */

    private String msg;
    private int code;
    /**
     * dsa : asd
     */

    private String status;

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", status='" + status + '\'' +
                '}';
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
