package net.iotsite.wekit.api.api;


import com.alibaba.fastjson.JSONObject;
import net.iotsite.wekit.api.api.impl.WeQRCodeRestImpl;
import net.iotsite.wekit.api.dto.Limit;
import net.iotsite.wekit.api.dto.Param;
import net.iotsite.wekit.common.utils.Result;

public interface WeQRCodeRest {

    /**
     * 生成带参数二维码
     *
     * @param limit
     * @param param
     * @return
     */
    Result qrCode(Limit limit, Param param);


    enum QrCodeType {

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

    default String createParams(Limit limit, Param param) {
        JSONObject map = new JSONObject();
        if (limit.isLimit()) {
            map.put("expire_seconds", limit.getLimitTime());
            if (param.isStr()) {
                map.put("action_name", WeQRCodeRestImpl.QrCodeType.QR_LIMIT_STR_SCENE);
            } else {
                map.put("action_name", WeQRCodeRestImpl.QrCodeType.QR_LIMIT_SCENE);
            }
        } else {
            if (param.isStr()) {
                map.put("action_name", WeQRCodeRestImpl.QrCodeType.QR_STR_SCENE);
            } else {
                map.put("action_name", WeQRCodeRestImpl.QrCodeType.QR_SCENE);
            }
        }
        map.put("action_info", param.getScene());
        return map.toJSONString();
    }

}
