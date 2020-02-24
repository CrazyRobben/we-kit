package net.iotsite.wekit.api.entity;

import com.alibaba.fastjson.JSONObject;

public class TextMessage extends CustomMessage {

    private String content;

    public TextMessage(String content) {
        this.content = content;
    }

    @Override
    public String getMsgType() {
        return MessageType.TEXT.getType();
    }

    @Override
    public JSONObject getMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        return jsonObject;
    }
}
