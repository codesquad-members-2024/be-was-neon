package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Decoder {
    private static final String DECODE_FORMAT = "UTF-8";

    public static String[] decodeUser(String[] userEncodedInfo) throws UnsupportedEncodingException {
        String[] userInfo = new String[userEncodedInfo.length];
        for (int index = 0; index < userInfo.length; index++) {
            userInfo[index] = decode(userEncodedInfo[index]);
        }
        return userInfo;
    }

    private static String decode(String originalStr) throws UnsupportedEncodingException {
        return URLDecoder.decode(originalStr, DECODE_FORMAT);
    }
}
