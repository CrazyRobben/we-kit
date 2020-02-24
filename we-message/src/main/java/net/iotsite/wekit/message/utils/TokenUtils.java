package net.iotsite.wekit.message.utils;


public class TokenUtils {

    public static String create(String openId) {
        String time = Long.toHexString(System.currentTimeMillis()).toUpperCase();
        return "TK" + openId.toUpperCase() + time;
    }
}
