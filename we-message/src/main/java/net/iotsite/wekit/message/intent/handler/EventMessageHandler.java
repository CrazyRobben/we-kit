package net.iotsite.wekit.message.intent.handler;

import com.alibaba.fastjson.JSONObject;
import net.iotsite.wekit.api.api.WeUserManageRest;
import net.iotsite.wekit.message.entity.EventMessage;
import net.iotsite.wekit.message.entity.EventType;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.message.entity.UserInfo;
import net.iotsite.wekit.message.mapper.UserMapper;
import net.iotsite.wekit.message.utils.MessageUtils;
import net.iotsite.wekit.message.utils.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 处理时间推送
 */
@Component("eventMessageHandler")
public class EventMessageHandler implements MessageHandler<EventMessage> {

    private static final Logger logger = LoggerFactory.getLogger(EventMessageHandler.class);

    @Value("${wekit.message.server}")
    private String messageServer;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeUserManageRest weUserManageRest;

    @Override
    public String handle(EventMessage message) {
        String event = message.getEvent();
        String fromUserName = message.getFromUserName();
        String toUserName = message.getToUserName();
        logger.info("handler:receive event:{},from user:{}", event, fromUserName);
        if (!EventType.SUBSCRIBE.equals(event)) {
            logger.debug("handle skip event");
            return "";
        }
        UserInfo userInfo = userMapper.queryByUserOpenId(fromUserName);
        if (null != userInfo) {
            return MessageUtils.textResponse(createRet(userInfo.getUserSecretKey()), toUserName, fromUserName);
        }
        Result result = weUserManageRest.userInfo(fromUserName);
        if (result.isSuccess()) {
            JSONObject jsonObject = (JSONObject) result.getData();
            userInfo = new UserInfo();
            userInfo.setUserOpenId(fromUserName);
            userInfo.setUserNickName(jsonObject.getString("nickname"));
            String secretKey = TokenUtils.create(fromUserName);
            userInfo.setUserSecretKey(secretKey);
            userMapper.save(userInfo);
            return MessageUtils.textResponse(createRet(secretKey), toUserName, fromUserName);
        }
        return MessageUtils.textResponse("Token获取失败", toUserName, fromUserName);
    }

    private String createRet(String secretKey) {
        String split = messageServer.endsWith("/") ? "" : "/";
        String message = "欢迎关注小助手！\n";
        String url = messageServer + split + "send/" + secretKey + "?text=your%20message";
        return message + url;
    }
}
