package net.iotsite.wekit.token.service;


import net.iotsite.wekit.token.TokenException;
import net.iotsite.wekit.token.WXToken;

public interface TokenService {


    void init() throws TokenException;

    /**
     * 申请token
     *
     * @return
     */
    WXToken apply() throws TokenException;
}
