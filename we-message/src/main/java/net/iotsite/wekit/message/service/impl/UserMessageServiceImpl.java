package net.iotsite.wekit.message.service.impl;

import net.iotsite.wekit.api.TokenAutowired;
import net.iotsite.wekit.api.api.WeCustomMessageRest;
import net.iotsite.wekit.api.entity.TextMessage;
import net.iotsite.wekit.common.utils.URLUtils;
import net.iotsite.wekit.message.entity.Message;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.message.entity.UserInfo;
import net.iotsite.wekit.message.intent.handler.MessageHandler;
import net.iotsite.wekit.message.intent.MessageHandlerFactory;
import net.iotsite.wekit.message.mapper.UserMapper;
import net.iotsite.wekit.message.service.UserMessageService;
import net.iotsite.wekit.message.utils.MessageParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UserMessageServiceImpl extends TokenAutowired implements UserMessageService {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageServiceImpl.class);


    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    @Value("${config.send.async:true}")
    private boolean async;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeCustomMessageRest weCustomMessageRest;


    @Override
    public String dealMessage(String message) {

        //消息解析
        Message parser = null;
        try {
            parser = MessageParser.toMessage(message);
        } catch (Exception e) {
            logger.debug("dealMessage:用户消息解析失败:\n{}", message);
            return "";
        }
        String fromUserName = parser.getFromUserName();
        String toUserName = parser.getToUserName();
        String msgType = parser.getMsgType();
        logger.info("dealMessage:from:{},to:{},type:{}", fromUserName, toUserName, msgType);

        MessageHandler messageHandler = messageHandlerFactory.create(parser);

        return messageHandler.handle(parser);
    }

    @Override
    public Result send(String secretKey, String message) {
        if (StringUtils.isBlank(message)) {
            Result.failed("发送消息不能为空");
        }
        message = URLUtils.encode(message);
        UserInfo userInfo = userMapper.queryByUserSecretKey(secretKey);
        if (null == userInfo || null == userInfo.getUserOpenId()) {
            return Result.failed("请先关注公众账号并且完成绑定");
        }
        String userOpenId = userInfo.getUserOpenId();
        if (async) {
            send(userOpenId, new TextMessage(message));
            return Result.success("OK");
        }

        try {
            return weCustomMessageRest.send(userOpenId, new TextMessage(message));
        } catch (Exception e) {
            logger.error("消息方式失败", e);
            return Result.failed("消息发送失败:" + e.getMessage());
        }
    }

    @Async
    public void send(String openid, TextMessage textMessage) {
        weCustomMessageRest.send(openid, textMessage);
    }
}
