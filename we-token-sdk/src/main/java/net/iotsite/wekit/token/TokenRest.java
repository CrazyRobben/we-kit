package net.iotsite.wekit.token;

import com.alibaba.fastjson.JSONObject;
import net.iotsite.wekit.common.utils.HttpUtils;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.common.utils.WeChatWrapper;

public class TokenRest {

    /**
     * 获取Token
     *
     * @param tokenServer
     * @return
     * @throws TokenException
     */
    public static WXToken getToken(String tokenServer) throws TokenException {
        HttpUtils.HttpResponse httpResponse = HttpUtils.doGet(tokenServer);
        Result wrapper = httpResponse.wrapper(new WeChatWrapper());
        if (!wrapper.isSuccess()) {
            throw new TokenException(wrapper.getMessage());
        }
        JSONObject data = (JSONObject) wrapper.getData();
        String accessToken = data.getString("access_token");
        Integer expiresIn = data.getInteger("expires_in");
        return new WXToken(accessToken, expiresIn);
    }

}
