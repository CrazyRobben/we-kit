package net.iotsite.wekit.api.api;


import net.iotsite.wekit.common.utils.Result;

public interface WeUserManageRest {

    /**
     * 通过openId查询用户消息
     *
     * @param openId
     * @return
     */
    Result userInfo(String openId);
}
