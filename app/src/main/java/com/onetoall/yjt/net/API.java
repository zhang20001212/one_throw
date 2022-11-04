package com.onetoall.yjt.net;

/**
 * Created by qinwei on 2016/10/20 4:12
 * email:qinwei_it@163.com
 */

public class API {
    public static final String API_VERSION = "1.1";

    public static String domain;
    //我的测试
    public static String ceshi;
    public static void setDomain(String domain) {
        API.domain = domain;
    }
    public static final String BIND_PUSH_ID = "/api/appAccount/uploadOneToken";
    public static final String LOGIN = "/api/appAccount/loginYjt";//登录
    public static final String FIND_USER_INFO = "/api/appAccount/queryUserInfo";//查询用户详情
    public static final String FIND_STORE_INFO = "/api/store/findStoreInfo";//查找门店信息

    public static final String ACCOUNT_BOOK_INDEX = "/api/amount/selectAmount";//查询账本列表
    public static final String FIND_PAY_ORDER_BY_ID = "/api/amount/selectOneAmount";//查询账本详情

    //二维码相关api
    public static final String GET_WEIXIN_QR_CODE = "/api/xlPay/xlScanCode_wx";//获取微信付款码
    public static final String GET_WEIXIN_QR_CODE_PAY = "/api/xlPay/xlPayBarCode";//微信扫码收款
    public static final String PUT_CASH_PAY = "/api/xlPay/cash";//提交现金支付

    public static final String GET_ALIPAY_QR_CODE = "/api/xlPay/xlScanCode_zfb";//获取支付宝付款码
    public static final String GET_ALIPAY_QR_CODE_PAY = "/api/xlPay/xlPayBarCode";//支付宝扫码收款

    public static final String FIND_QR_CODE_LIST = "/api/xlPay/ymf_manager_xl";//查询一码付列表
    public static final String CHANGE_PWD = "/api/appAccount/updatePw";//修改密码
    public static final String SELECT_MERSTOREINFO = "/api/store/select_merStoreInfo";//查询商户信息
    public static final String  CHANGEUSERINFO = "/api/appAccount/changeUserInfo";//修改个人信息
    public static final String QUERY_ALL_INFONEW = "/api/appAccount/queryAllInfoNew";//实时查询用户信息


    public static final String CHANNEL_RATIO = "/api/amount/statistics";//各渠道占比
    public static final String RESETPW = "/api/appAccount/resetPw";//忘记密码重置
    public static final String SEND_VERFI_MSG = "/api/appAccount/forgetPw";//发送短信

    public static final String BIND_QR_CODE = "/api/xlPay/ymf_binding";//绑定什码付

    public static final String COLLECTION_MONEY = "/api/store/customInfo";//客户列表

    public static final String PERSONNEL_MANAGEMENT = "/api/appAccount/queryAllInfoNew";//人员管理
    public static final String ANDROID_VERSION_NEW = "/api/version/android_version_new";//人员管理
    public static final String COMMIT_UPDATA ="/api/notice/updatestatus";//修改消息
    public static final String MESSAGE_LIST = "/api/notice/select_notice";//消息列表

    private static final String SELECT_MESSAGE_READ_STATUS ="/api/notice/select_noread_message" ;
    public static String loadLogin() {
        return domain + LOGIN;
    }

    public static String loadAccountBookIndex() {
        return domain + ACCOUNT_BOOK_INDEX;
    }

    public static String loadStoreInfo() {
        return domain + FIND_STORE_INFO;
    }


    public static String loadUserInfo() {
        return domain + FIND_USER_INFO;
    }

    public static String loadPayOrderById() {
        return domain + FIND_PAY_ORDER_BY_ID;
    }

    public static String loadQRCodes() {
        return domain + FIND_QR_CODE_LIST;
    }

    public static String loadCollectionMoney(){
        return domain + COLLECTION_MONEY;
    }

    /**
     * 修改消息
     * @return
     */
    public static String commitUpdata(){
        return domain +COMMIT_UPDATA;
    };

    /**
     * 获取人员管理
     * @return
     */
    public static String loadPersonnelManagement(){
        return domain + PERSONNEL_MANAGEMENT;
    }

    /**
     * 支付宝扫码收款
     *
     * @return
     */
    public static String loadAlipayQRCodePay() {
        return domain + GET_ALIPAY_QR_CODE_PAY;
    }

    /**
     * 获取支付宝付款码
     *
     * @return
     */
    public static String loadAlipayQRCode() {
        return domain + GET_ALIPAY_QR_CODE;
    }

    /**
     * 微信扫码收款
     *
     * @return
     */
    public static String loadWeixinQRCodePay() {
        return domain + GET_WEIXIN_QR_CODE_PAY;
    }

    /**
     * 获取微信付款码
     *
     * @return
     */
    public static String loadWeixinQRCode() {
        return domain + GET_WEIXIN_QR_CODE;
    }

    /**
     * 现金记账
     */
    public static String putCashPay() {
        return domain + PUT_CASH_PAY;
    }

    /**
     * 修改密码
     */
    public static String changePwd() {
        return domain + CHANGE_PWD;
    }

    /**
     * 查询商户
     */
    public static String selectMerstoreInfo() {
        return domain + SELECT_MERSTOREINFO;
    }

    /**
     * 查询商户
     */
    public static String changeUserInfo() {
        return domain + CHANGEUSERINFO;
    }


    /**
     * 绑定二维码
     *
     * @return
     */
    public static String loadBindQrCode() {
        return domain + BIND_QR_CODE;
    }

    public static String getChannelRatio() {
        return domain + CHANNEL_RATIO;
    }

    /**
     * 忘记密码
     */
    public static String resetPwd() {
        return domain + RESETPW;
    }

    /**
     * 发送短信
     */
    public static String sendVerfiMsg() {
        return domain + SEND_VERFI_MSG;
    }
    /**
     * 实时查询用户信息
     */
    public static String querryAllInfoNew() {
        return domain + QUERY_ALL_INFONEW;
    }

    public static String loadPushId() {
        return domain + BIND_PUSH_ID;
    }
    /**
     * 查询版本信息
     */
    public static String updataApk() {
        return domain + ANDROID_VERSION_NEW;
    }
    public static String getMessageList() {return domain + MESSAGE_LIST;}

    /**
     * 是否有已读消息
     * @return
     */
    public static String loadMessageReadStatus() {
        return domain+SELECT_MESSAGE_READ_STATUS;
    }
}
