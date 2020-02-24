package net.iotsite.wekit.api;

import net.iotsite.wekit.api.api.impl.WeQRCodeRestImpl;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.token.TokenException;
import net.iotsite.wekit.token.WXToken;
import net.iotsite.wekit.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TokenAutowired {

    private TokenService tokenService;

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    public String getToken() throws TokenException {
        WXToken apply = tokenService.apply();
        return apply.getAccessToken();
    }

}
