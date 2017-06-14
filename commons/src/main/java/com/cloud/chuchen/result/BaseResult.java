package com.cloud.chuchen.result;

import org.apache.commons.collections.map.HashedMap;

import java.io.Serializable;
import java.util.Map;


public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 2998797498624089961L;

    private String code;

    private String msg;

    private T data;

    public BaseResult(String code,  String msg,T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> BaseResult<T> success() {
        return new BaseResult<T>("0000", "成功", null);
    }

    public static <T> BaseResult<T> success(T data) {
        return new BaseResult<T>("0000", "成功", data);
    }

    public static <T> BaseResult<T> error(String code) {
        String msg = ResultCode.getMsg(code);

        return new BaseResult<>(code, msg, null);
    }

    public static <T> BaseResult<T> error(String code, String msg) {

        return new BaseResult<>(code, msg, null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getAll() {
        Map<String, Object> map = new HashedMap();
        if (code == null) {
            map.put("code", "0000");
        } else {
            map.put("code", code);
        }
        if (msg == null) {
            map.put("msg", "");
        } else {
            map.put("msg", msg);
        }

        if (data == null) {
            map.put("data", "");
        } else {
            map.put("data", data);
        }
        return GsonUtils.objectToJson(map);
    }
}