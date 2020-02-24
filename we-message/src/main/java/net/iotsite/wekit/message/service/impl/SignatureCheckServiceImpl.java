package net.iotsite.wekit.message.service.impl;

import net.iotsite.wekit.message.service.SignatureCheckService;
import net.iotsite.wekit.message.utils.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SignatureCheckServiceImpl implements SignatureCheckService {


    /**
     * 服务器token
     */
    @Value("${config.wechat.token:wechat-token}")
    private String token;

    private static final Logger logger = LoggerFactory.getLogger(SignatureCheckServiceImpl.class);


    @Override
    public String check(String signature, String timestamp, String nonce, String echoStr) {
        logger.debug("");
        if (StringUtils.isAnyBlank(signature, timestamp, nonce)) {
            logger.warn("参数异常signature,timestamp,nonce都不能为空");
            return "参数异常signature,timestamp,nonce都不能为空";
        }
        if (SignUtil.checkSignature(token, signature, timestamp, nonce)) {
            logger.debug("Token校验成功");
            return echoStr;
        } else {
            logger.warn("Token验证失败");
            return "Token验证失败";
        }
    }
}
