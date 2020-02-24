package net.iotsite.wekit.token.service;

import net.iotsite.wekit.token.TokenException;
import net.iotsite.wekit.token.service.impl.RemoteTokenServiceImpl;
import net.iotsite.wekit.token.service.impl.TokenCacheImpl;
import org.apache.http.util.Asserts;

public class TokenCacheBuilder {

    private static final String wechatServer = "https://api.weixin.qq.com/cgi-bin/token";

    public static TokenService builder(String server) throws TokenException {
        Asserts.notNull(server, "tokenServer");
        if (server.startsWith(wechatServer)) {
            TokenCacheImpl tokenCache = new TokenCacheImpl();
            tokenCache.setTokenServer(server);
            tokenCache.init();
            return tokenCache;
        } else {
            RemoteTokenServiceImpl remoteTokenService = new RemoteTokenServiceImpl();
            remoteTokenService.setTokenServer(server);
            remoteTokenService.init();
            return remoteTokenService;
        }
    }

    public static TokenService builder(String appId, String appSecret) {
        Asserts.notNull(appId, "appId");
        Asserts.notNull(appSecret, "appSecret");
        TokenCacheImpl tokenCache = new TokenCacheImpl();
        String url = wechatServer + "?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        tokenCache.setTokenServer(url);
        return tokenCache;
    }

}
