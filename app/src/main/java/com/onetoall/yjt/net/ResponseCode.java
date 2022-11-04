package com.onetoall.yjt.net;

/**
 * 服务响应code
 * Created by qinwei on 2016/11/15 9:14
 * email:qinwei_it@163.com
 */

public class ResponseCode {
    public static final int CODE_SUCCESS = 0;//添加成功
    public static final int CODE_NO_LOGIN = 100;//未登录
    public static final int CODE_NO_DATA = 1001;//没有数据
    public static final int CODE_ERROR_REQUEST = 1002;//错误请求

    public static final int CODE_SERVER_UPDATE = 40001;//服务器维护
    public static final int CODE_TOKEN_NOT_VALIDATE = 1016;//token失效
    public static final int CODE_PAY_ING = 1024;//支付处理中

    public static final int CODE_ORDER_CANCEL = 2001;
//    public static final int user_is_exist=1024

}
