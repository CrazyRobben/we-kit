package net.iotsite.wekit.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLUtils {
    private static final String UTF_8 = "UTF-8";

    public static String encode(String url) {
        try {
            return URLEncoder.encode(url, UTF_8);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    public static String decode(String url) {
        try {
            return URLDecoder.decode(url, UTF_8);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
