package net.iotsite.wekit.message.intent.handler;

import net.iotsite.wekit.message.entity.Message;

public interface MessageHandler<T extends Message> {

    String handle(T message);
}
