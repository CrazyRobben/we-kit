package net.iotsite.wekit.message.intent;


import net.iotsite.wekit.message.entity.EventMessage;
import net.iotsite.wekit.message.entity.Message;
import net.iotsite.wekit.message.entity.UserMessage;
import net.iotsite.wekit.message.intent.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageHandlerFactory {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerFactory.class);

    /**
     * 推送事件消息处理器
     */
    @Resource(name = "eventMessageHandler")
    private MessageHandler eventMessageHandler;

    /**
     * 用户消息处理器
     */
    @Resource(name = "userMessageHandler")
    private MessageHandler userMessageHandler;

    /**
     * 其他类型消息处理器
     */
    private MessageHandler defaultHandler;

    /*
     * 默认的消息处理
     */ {
        defaultHandler = new MessageHandler() {
            private final Logger logger = LoggerFactory.getLogger(this.getClass());

            @Override
            public String handle(Message message) {
                this.logger.info("handle:no handler for message,id:{},type:{}", message.getMsgId(), message.getMsgType());
                return "";
            }
        };
    }


    /**
     * 根据消息类型获取messageHandler
     *
     * @param message
     * @return
     */
    public MessageHandler create(Message message) {
        if (message instanceof EventMessage) {
            return eventMessageHandler;
        } else if (message instanceof UserMessage) {
            return userMessageHandler;
        } else {
            return defaultHandler;
        }
    }
}
