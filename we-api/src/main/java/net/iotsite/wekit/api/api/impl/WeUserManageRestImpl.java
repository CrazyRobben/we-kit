package net.iotsite.wekit.api.api.impl;

import lombok.extern.slf4j.Slf4j;
import net.iotsite.wekit.api.TokenAutowired;
import net.iotsite.wekit.api.api.WeUserManageRest;
import net.iotsite.wekit.common.utils.WeChatWrapper;
import net.iotsite.wekit.common.utils.HttpUtils;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.token.TokenException;
import net.iotsite.wekit.token.WXToken;
import net.iotsite.wekit.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class WeUserManageRestImpl extends TokenAutowired implements WeUserManageRest {

    private static final String USER_INFO_REST = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    @Autowired
    private TokenService tokenService;


    public Result userInfo(String openId) {
        WXToken token = null;
        try {
            token = tokenService.apply();
        } catch (TokenException e) {
            return Result.failed("Token获取失败");
        }

        HttpUtils.HttpResponse httpResponse = HttpUtils.doGet(USER_INFO_REST.replace("ACCESS_TOKEN", getToken()).replace("OPENID", openId));
        return httpResponse.wrapper(new WeChatWrapper());
    }
}
