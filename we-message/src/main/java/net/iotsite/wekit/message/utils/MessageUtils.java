package net.iotsite.wekit.message.utils;

import net.iotsite.wekit.common.utils.XmlUtil;
import net.iotsite.wekit.message.entity.TextMessage;

public class MessageUtils {

    /**
     * 生成文本类响应消息
     *
     * @param text
     * @param fromUser
     * @param toUser
     * @return
     */
    public static String textResponse(String text, String fromUser, String toUser) {
        TextMessage textMessage = new TextMessage();
        textMessage.setContent(text);
        textMessage.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000));
        textMessage.setMsgType("text");
        textMessage.setToUserName(toUser);
        textMessage.setFromUserName(fromUser);
        return XmlUtil.toXml(textMessage);
    }
}
