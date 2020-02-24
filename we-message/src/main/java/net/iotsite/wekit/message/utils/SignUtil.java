package net.iotsite.wekit.message.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignUtil {

    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);


    private SignUtil() {
    }

    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        sort(arr);
        StringBuilder content = new StringBuilder();

        for (String anArr : arr) {
            content.append(anArr);
        }

        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
        }

        return tmpStr != null && tmpStr.equalsIgnoreCase(signature.toUpperCase());
    }

    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();

        for (byte aByteArray : byteArray) {
            strDigest.append(byteToHexStr(aByteArray));
        }

        return strDigest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[]{Digit[mByte >>> 4 & 15], Digit[mByte & 15]};
        return new String(tempArr);
    }

    private static void sort(String[] a) {
        for (int i = 0; i < a.length - 1; ++i) {
            for (int j = i + 1; j < a.length; ++j) {
                if (a[j].compareTo(a[i]) < 0) {
                    String temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
        }

    }
}
