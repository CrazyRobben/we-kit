package net.iotsite.wekit.api.api;

import net.iotsite.wekit.api.entity.CustomMessage;
import net.iotsite.wekit.common.utils.Result;

public interface WeCustomMessageRest {

    Result send(String toUserId, CustomMessage message);

}
