package net.iotsite.wekit.message.intent.handler;

import net.iotsite.wekit.message.entity.TextMessage;
import net.iotsite.wekit.message.entity.UserMessage;
import net.iotsite.wekit.message.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component("userMessageHandler")
public class UserMessageHandler implements MessageHandler<UserMessage> {

    private static final Logger logger = LoggerFactory.getLogger(UserMessageHandler.class);


    @Override
    public String handle(UserMessage message) {
        String fromUserName = message.getFromUserName();
        String toUserName = message.getToUserName();
        String createTime = message.getCreateTime();

        if (!(message instanceof TextMessage)) {
            logger.debug("handle:不支持的消息类型,暂时只能处理文本消息");
            return "小助手不懂你的意思呢";
        }

        TextMessage textMessage = (TextMessage) message;

        //暂时把消息回给
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(createTime) * 1000));

        String retMessage = "From:" + fromUserName + "\nTo:" + toUserName + "\nTime:" + format + "\nMessage:" + textMessage.getContent();
        return MessageUtils.textResponse(retMessage, toUserName, fromUserName);
    }


}
