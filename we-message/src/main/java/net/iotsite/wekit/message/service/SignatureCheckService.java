package net.iotsite.wekit.message.service;

public interface SignatureCheckService {

    String check(String signature, String timestamp, String nonce, String echostr);

}
