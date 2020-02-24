package net.iotsite.wekit.message.utils;

import com.alibaba.fastjson.serializer.SerializerFeature;
import net.iotsite.wekit.common.utils.XmlUtil;
import net.iotsite.wekit.message.entity.EventMessage;
import net.iotsite.wekit.message.entity.Message;
import net.iotsite.wekit.message.entity.TextMessage;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MessageParser {

    private static final Logger logger = LoggerFactory.getLogger(MessageParser.class);


    private static final Map<String, Class<? extends Message>> MESSAGE;

    static {
        //以MsgType+Event为key
        MESSAGE = new HashMap<>();
        MESSAGE.put("text", TextMessage.class);
        MESSAGE.put("eventsubscribe", EventMessage.class);
        MESSAGE.put("eventunsubscribe", EventMessage.class);
    }


    @SuppressWarnings("unchecked")
    public static Message toMessage(String xml) throws DocumentException {
        JSONObject jsonObject = XmlUtil.toObject(xml);
        logger.debug("toMessage:\n{}", jsonObject.toString(SerializerFeature.PrettyFormat));
        //根据消息类型获取Class
        String msgType = (String) jsonObject.getOrDefault("MsgType", "");
        String event = (String) jsonObject.getOrDefault("Event", "");
        Class<? extends Message> orDefault = MESSAGE.getOrDefault(msgType + event, Message.class);
        return jsonObject.toJavaObject(orDefault);
    }

}
