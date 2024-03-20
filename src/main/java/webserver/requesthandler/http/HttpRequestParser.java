package webserver.requesthandler.http;


import static webserver.requesthandler.http.HttpConst.HEADER_DELIMITER;
import static webserver.requesthandler.http.HttpConst.HTML_FORM_CONTENT_TYPE;
import static webserver.requesthandler.http.HttpConst.PARAMS_DELIMITER;
import static webserver.requesthandler.http.HttpConst.PARAMS_LENGTH;
import static webserver.requesthandler.http.HttpConst.PARAM_DELIMITER;
import static webserver.requesthandler.http.HttpConst.QUERY_COMMAND_START;
import static webserver.requesthandler.http.HttpConst.START_LINE_DELIMITER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        HttpRequest request = new HttpRequest();

        // 요청 라인 파싱
        String[] requestLineParts = extractRequestLineParts(br);
        request.setMethod(requestLineParts[0]);
        String url = URLDecoder.decode(requestLineParts[1], StandardCharsets.UTF_8);
        request.setPath(extractPath(url));

        // 헤더 파싱
        Map<String, String> headers = extractHeaders(br);
        request.setHeaders(headers);

        // 본문 파싱
        String body = extractBody(br, headers);
        request.setBody(body);

        // 파람 파싱
        request.setParams(extractParams(url, headers, body));

        return request;
    }

    private static String[] extractRequestLineParts(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        return requestLine.split(START_LINE_DELIMITER);
    }

    private static String extractPath(String url) {
        if (url.contains(QUERY_COMMAND_START)) {
            return url.substring(0, url.indexOf(QUERY_COMMAND_START));
        }
        return url;
    }


    private static Map<String, String> extractHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(HEADER_DELIMITER);
            headers.put(headerParts[0].trim(), headerParts[1].trim());
        }
        return headers;
    }

    private static String extractBody(BufferedReader br, Map<String, String> headers) throws IOException {
        if (headers.containsKey(HttpConst.HEADER_CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(headers.get(HttpConst.HEADER_CONTENT_LENGTH));
            char[] body = new char[contentLength];
            br.read(body);
            return new String(body);
        }
        return HttpConst.EMPTY_STRING;
    }

    private static Map<String, String> extractParams(String url, Map<String, String> headers, String body) {
        Map<String, String> params = extractQueryParams(url);
        params.putAll(extractBodyParams(headers, body));
        return params;
    }

    private static Map<String, String> extractQueryParams(String url) {
        Map<String, String> params = new HashMap<>();
        if (url.contains(QUERY_COMMAND_START)) {
            String queryString = url.substring(url.indexOf(QUERY_COMMAND_START) + 1);
            parseParams(queryString, params);
        }
        return params;
    }

    private static Map<String, String> extractBodyParams(Map<String, String> headers, String body) {
        Map<String, String> params = new HashMap<>();
        String contentType = headers.get(HttpConst.HEADER_CONTENT_TYPE);
        if (contentType != null && contentType.equals(HTML_FORM_CONTENT_TYPE)) {
            String decodedBody = URLDecoder.decode(body, StandardCharsets.UTF_8);
            parseParams(decodedBody, params);
        }
        return params;
    }

    private static void parseParams(String paramString, Map<String, String> paramsMap) {
        if (paramString == null || paramString.isEmpty()) {
            return;
        }

        String[] paramParts = paramString.split(PARAMS_DELIMITER);
        for (String param : paramParts) {
            String[] keyValue = param.split(PARAM_DELIMITER);
            if (keyValue.length == PARAMS_LENGTH) {
                String key = keyValue[0];
                String value = keyValue[1];
                paramsMap.put(key, value);
            }
        }
    }
}
