package net.iotsite.wekit.message.service;

import net.iotsite.wekit.common.utils.Result;

public interface UserMessageService {

    String dealMessage(String message);

    Result send(String token, String message);

}
