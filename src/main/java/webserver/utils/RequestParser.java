package webserver.utils;

import exception.CustomException;
import exception.server.MalformedBodyFormatException;
import webserver.type.HttpStatusCode;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParser {
    public static final String CHARSET = PropertyUtils.loadProperties().getProperty("charset");

    private RequestParser() {

    }

    // TODO: value가 ,으로 구분된 경우 고려
    public static Map<String, List<String>> parseHeader(List<String> requestHeader) {
        Map<String, List<String>> headerMap = new HashMap<>();

        for (String headerLine : requestHeader) {
            String[] keyValue = headerLine.split(":", 2);
            String headerKey = keyValue[0].trim();
            List<String> headerValues = new ArrayList<>();
            for (String headerValue : keyValue[1].split(";")) {
                headerValues.add(headerValue.trim());
            }
            headerMap.put(headerKey, headerValues);
        }

        return headerMap;
    }

    public static Map<String, String> parseBody(String body) {
        if (body.isEmpty()) {
            throw new IllegalArgumentException("요청 바디가 존재하지 않습니다.");
        }

        try{
            byte[] bytes = new byte[body.length()];
            for (int i = 0; i < body.length(); i++) {
                bytes[i] = (byte) body.charAt(i);
            }
            body = new String(bytes, CHARSET);

            Map<String, String> bodyMap = new HashMap<>();

            for (String entry : body.split("&")) {
                String[] keyValue = entry.split("=", 2);
                String key = keyValue[0];

                if (keyValue.length != 2 || key.isBlank()) {
                    throw new MalformedBodyFormatException();
                }

                String value = keyValue[1];
                bodyMap.put(key, value);
            }

            return bodyMap;
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(HttpStatusCode.INTERNAL_SERVER_ERROR, e);
        }
    }
}
