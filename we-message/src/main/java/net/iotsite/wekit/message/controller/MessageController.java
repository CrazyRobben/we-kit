package net.iotsite.wekit.message.controller;

import com.alibaba.fastjson.JSONObject;
import net.iotsite.wekit.api.api.WeQRCodeRest;
import net.iotsite.wekit.api.api.impl.WeQRCodeRestImpl;
import net.iotsite.wekit.common.utils.HttpUtils;
import net.iotsite.wekit.common.utils.Result;
import net.iotsite.wekit.common.utils.URLUtils;
import net.iotsite.wekit.message.service.SignatureCheckService;
import net.iotsite.wekit.message.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;


@RestController
public class MessageController {

    private static final String QR_CODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    @Autowired
    private SignatureCheckService signatureCheckService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private WeQRCodeRest qrCodeRest;

    /**
     * GET 请求用于校验服务器可用性
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping("/message")
    public String checkSignature(String signature, String timestamp, String nonce, String echostr) {
        return signatureCheckService.check(signature, timestamp, nonce, echostr);
    }

    /**
     * POST 请求用于响应接收和响应用户发送来的消息
     *
     * @param message
     * @return
     */
    @PostMapping("/message")
    public String receiveMessage(@RequestBody String message) {
        return userMessageService.dealMessage(message);
    }


    /**
     * POST 请求用于响应接收和响应用户发送来的消息
     *
     * @param text
     * @return
     */
    @GetMapping("/send/{secretKey}")
    public Result send(@PathVariable String secretKey, @RequestParam String text) {
        return userMessageService.send(secretKey, text);
    }

    /**
     * POST 请求用于响应接收和响应用户发送来的消息
     *
     * @return
     */
    @GetMapping("/loginCode")
    public Result qrCodeImage() throws IOException {
        Result result = qrCodeRest.qrCode(new WeQRCodeRestImpl.Limit(300), new WeQRCodeRestImpl.Param(Long.toHexString(System.currentTimeMillis())));
        if (!result.isSuccess()) {
            return result;
        }
        JSONObject data = (JSONObject) result.getData();
        data.put("qr_code_url", QR_CODE_URL + URLUtils.encode(data.getString("ticket")));
        return result;
    }

    /**
     * POST 请求用于响应接收和响应用户发送来的消息
     *
     * @return
     */
    @PostMapping("/loginCode")
    public Result qrCodeUrl() {
        return qrCodeRest.qrCode(new WeQRCodeRestImpl.Limit(300), new WeQRCodeRestImpl.Param(Long.toHexString(System.currentTimeMillis())));
    }

}
