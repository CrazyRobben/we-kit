package net.iotsite.wekit.api.api.impl;

import net.iotsite.wekit.api.TokenAutowired;
import net.iotsite.wekit.api.api.WeQRCodeRest;
import net.iotsite.wekit.api.dto.Limit;
import net.iotsite.wekit.api.dto.Param;
import net.iotsite.wekit.common.utils.HttpUtils;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.common.utils.WeChatWrapper;
import org.springframework.stereotype.Service;

@Service
public class WeQRCodeRestImpl extends TokenAutowired implements WeQRCodeRest {


    private static final String QR_CODE_REST = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";


    @Override
    public Result qrCode(Limit limit, Param param) {
        String params = createParams(limit, param);
        HttpUtils.HttpResponse response = HttpUtils.doPost(QR_CODE_REST.replace("ACCESS_TOKEN", getToken()), params, HttpUtils.BodyType.JSON);
        return response.wrapper(new WeChatWrapper());
    }
}
