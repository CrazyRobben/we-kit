package net.iotsite.wekit.token.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.iotsite.wekit.common.utils.DateUtils;
import net.iotsite.wekit.token.TokenException;
import net.iotsite.wekit.token.TokenRest;
import net.iotsite.wekit.token.WXToken;
import net.iotsite.wekit.token.WXToken.TokenStatus;
import net.iotsite.wekit.token.service.TokenService;

import javax.annotation.PostConstruct;

@Slf4j
public class TokenCacheImpl implements TokenService {

    private String tokenServer;

    private transient WXToken wxToken;

    public void setTokenServer(String tokenServer) {
        this.tokenServer = tokenServer;
    }


    public void init() throws TokenException {
        wxToken = TokenRest.getToken(tokenServer);
        log.info("init:{}", wxToken);
        new Thread(new RefreshClass()).start();
    }

    @Override
    public WXToken apply() {
        return wxToken;
    }

    /*@Override*/
    public WXToken refresh() {
        try {
            WXToken token = TokenRest.getToken(tokenServer);
        } catch (TokenException e) {
            e.printStackTrace();
        }
        return wxToken;
    }

    private class RefreshClass implements Runnable {

        @Override
        public void run() {
            RetryPolicy retryPolicy = new RetryPolicy();
            for (; ; ) {
                TokenStatus status = wxToken.getStatus();
                //如果Token即将过期则开始刷新Token
                if (status != TokenStatus.EFFECT) {
                    try {
                        if (!retryPolicy.effect) {
                            retryPolicy = new RetryPolicy();
                        }
                        wxToken = TokenRest.getToken(tokenServer);
                        log.info("Token更新成功,失效时间:{},Token:{}", DateUtils.strFormat(wxToken.getExpiresTime()), wxToken.getAccessToken());
                        retryPolicy.effect = false;
                    } catch (Exception e) {
                        log.warn("Token更新失败：{}", e.getMessage());
                        try {
                            Thread.sleep(retryPolicy.getSleepTimeMs());
                        } catch (InterruptedException ignored) {

                        }
                    }
                } else {
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }


    private static class RetryPolicy {

        private boolean effect = true;

        private int retryTimes = 0;

        private long retryIntervalMs = 1000L;

        public long getSleepTimeMs() {
            if (retryIntervalMs >= 1000 * 60) {
                return retryIntervalMs;
            }
            retryIntervalMs = retryIntervalMs + retryTimes++ * 1000;
            return retryIntervalMs;
        }
    }

}
