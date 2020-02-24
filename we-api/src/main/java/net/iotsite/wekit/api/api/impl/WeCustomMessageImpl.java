package net.iotsite.wekit.api.api.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.iotsite.wekit.api.TokenAutowired;
import net.iotsite.wekit.api.api.WeCustomMessageRest;
import net.iotsite.wekit.api.entity.CustomMessage;
import net.iotsite.wekit.common.utils.HttpUtils;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.common.utils.WeChatWrapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeCustomMessageImpl extends TokenAutowired implements WeCustomMessageRest {

    private static final String SENT_MESSAGE_REST = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    @Override
    public Result send(String toUserId, CustomMessage message) {
        String msgType = message.getMsgType();
        JSONObject msg = message.getMessage();
        JSONObject params = new JSONObject();
        params.put("touser", toUserId);
        params.put("msgtype", msgType);
        params.put(msgType, msg);
        log.debug("send:toUser:{},msgType:{},msg:{}", toUserId, msgType, msg.toString());
        HttpUtils.HttpResponse response = HttpUtils.doPost(SENT_MESSAGE_REST.replace("ACCESS_TOKEN", getToken()), params.toJSONString(), HttpUtils.BodyType.JSON);
        return response.wrapper(new WeChatWrapper());
    }
}
