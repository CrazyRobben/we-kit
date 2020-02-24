package net.iotsite.wekit.token.service.impl;


import net.iotsite.wekit.token.TokenException;
import net.iotsite.wekit.token.TokenRest;
import net.iotsite.wekit.token.WXToken;
import net.iotsite.wekit.token.service.TokenService;

public class RemoteTokenServiceImpl implements TokenService {

    private String tokenServer;

    public void setTokenServer(String tokenServer) {
        this.tokenServer = tokenServer;
    }

    @Override
    public void init() throws TokenException {

    }

    @Override
    public WXToken apply() throws TokenException {
        return TokenRest.getToken(tokenServer);
    }
}
