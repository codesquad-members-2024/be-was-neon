package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Decoder {
    private static final String DECODE_FORMAT = "UTF-8";

    public static String decode(String originalStr) throws UnsupportedEncodingException {
        return URLDecoder.decode(originalStr, DECODE_FORMAT);
    }
}
