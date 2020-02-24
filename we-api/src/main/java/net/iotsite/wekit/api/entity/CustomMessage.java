package net.iotsite.wekit.api.entity;

import com.alibaba.fastjson.JSONObject;

public abstract class CustomMessage {

    public abstract String getMsgType();

    public abstract JSONObject getMessage();
}
