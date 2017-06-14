package com.cloud.chuchen.result;

public class ResultCode {

    /**
     * 成功
     */
    public static final String SUCCESS = "0000";
    /**
     * 接口不存在
     */
    public static final String INTERFACE_NOTEXIST = "1002";
    /**
     * 登录授权异常
     */
    public static final String LOGIN_FAIL = "1004";
    /**
     * 系统异常
     */
    public static final String SGW_WRONG = "9999";


    /**
     * 通过状态拿到msg
     *
     * @param status 状态
     * @return
     */


    public static String getMsg(String status) {

        String msg;

        switch (status) {
            case SUCCESS:
                msg = "成功";
                break;
            case INTERFACE_NOTEXIST:
                msg = "接口不存在";
                break;
            default:
                msg = "系统异常";
        }

        return msg;
    }
}