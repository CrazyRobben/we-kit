package net.iotsite.wekit.api.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import lombok.Getter;
import net.iotsite.wekit.api.TokenAutowired;
import net.iotsite.wekit.api.api.WeQRCodeRest;
import net.iotsite.wekit.common.utils.HttpUtils;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.common.utils.WeChatWrapper;
import org.springframework.stereotype.Service;

@Service
public class WeQRCodeRestImpl extends TokenAutowired implements WeQRCodeRest {


    private static final String QR_CODE_REST = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";

    private enum QrCodeType {

        //永久二维码 整型参数
        QR_SCENE("QR_SCENE"),
        //永久二维码 字符串参数
        QR_STR_SCENE("QR_STR_SCENE"),
        //临时二维码 整型参数
        QR_LIMIT_SCENE("QR_LIMIT_SCENE"),
        //临时二维码 字符串参数
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE");

        private String type;

        public String getType() {
            return type;
        }

        QrCodeType(String type) {
            this.type = type;
        }
    }


    @Override
    public Result qrCode(Limit limit, Param param) {
        JSONObject map = new JSONObject();
        if (limit.isLimit()) {
            map.put("expire_seconds", limit.limitTime);
            if (param.isStr()) {
                map.put("action_name", QrCodeType.QR_LIMIT_STR_SCENE);
            } else {
                map.put("action_name", QrCodeType.QR_LIMIT_SCENE);
            }
        } else {
            if (param.isStr()) {
                map.put("action_name", QrCodeType.QR_STR_SCENE);
            } else {
                map.put("action_name", QrCodeType.QR_SCENE);
            }
        }
        map.put("scene", param.getScene());
        HttpUtils.HttpResponse response = HttpUtils.doPost(QR_CODE_REST.replace("ACCESS_TOKEN", getToken()), map.toJSONString(), HttpUtils.BodyType.JSON);
        return response.wrapper(new WeChatWrapper());
    }

    @Getter
    public static class Param {

        private Integer sceneId;

        private String sceneStr;

        public Param(Integer sceneId) {
            this.sceneId = sceneId;
        }

        public Param(String sceneStr) {
            this.sceneStr = sceneStr;
        }

        public boolean isStr() {
            return null == sceneStr;
        }

        public JSONObject getScene() {
            JSONObject scene = new JSONObject();
            if (isStr()) {
                scene.put("scene_str", sceneStr);
            } else {
                scene.put("scene_id", sceneId);
            }
            return scene;
        }
    }

    @Getter
    public static class Limit {

        private boolean limit;

        private Integer limitTime;

        public Limit(Integer limitTime) {
            limit = true;
            this.limitTime = limitTime;
        }

        public Limit(boolean limit) {
            this.limit = limit;
        }
    }
}
