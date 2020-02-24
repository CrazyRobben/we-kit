package net.iotsite.wekit.common.utils;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class Result implements Serializable {

    private int code;

    private String message;


    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private Object data;

    public Result(Object data) {
        this.data = data;
    }

    public static Result success(Object data) {
        return new Result(0, "执行成功", data);
    }

    public static Result failed(Integer code, String message) {
        return new Result(code, message, null);
    }

    public static Result failed(String message) {
        return new Result(1, message, null);
    }

    public boolean isSuccess() {
        return code == 0;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
