package net.iotsite.wekit.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import net.iotsite.wekit.common.utils.DateUtils;

import java.util.Date;

@Getter
@Setter
public class WXToken {

    /**
     * 提前刷新Token的毫秒数
     */
    private static final int MAX_EXPIRE = 10 * 60 * 1000;

    private static final int MIN_EXPIRE = 20 * 1000;

    public WXToken(String accessToken, Integer expiresIn) {
        this.accessToken = accessToken;
        this.expiresTime = new Date(System.currentTimeMillis() + (expiresIn - 5) * 1000);
    }

    /**
     * TOKEN
     */
    @JSONField(name = "access_token")
    private String accessToken;

    /**
     * 过期时间
     */
    @JSONField(name = "expires_time")
    private Date expiresTime;

    @JSONField(name = "expires_in")
    public Integer getExpireIn() {
        return (int) (DateUtils.timeDiff(expiresTime, new Date()) / 1000);
    }

    public String toJSONSting() {
        return JSON.toJSONString(this);
    }


    @Override
    public String toString() {
        return "失效时间：" + DateUtils.strFormat(expiresTime) + ",Token:" + accessToken;
    }


    public enum TokenStatus {
        EFFECT, INVALID, NEAR_INVALID;
    }

    @JSONField(serialize = false)
    public TokenStatus getStatus() {
        long expireTime = this.getExpiresTime().getTime();
        long nowTime = System.currentTimeMillis();
        long expiresIn = expireTime - nowTime;

        //有效时间小于 MIN_EXPIRE 为已经失效
        if (expiresIn < MIN_EXPIRE) {
            return TokenStatus.INVALID;
        }

        //有效时间大于 MAX_EXPIRE 为有效状态
        if (expiresIn > MAX_EXPIRE) {
            return TokenStatus.EFFECT;
        }
        //其余情况为即将失效
        return TokenStatus.NEAR_INVALID;
    }
}
