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
import webserver.requesthandler.http.message.Body;
import webserver.requesthandler.http.message.Headers;
import webserver.requesthandler.http.message.Parameters;
import webserver.requesthandler.http.message.RequestLine;

public class HttpRequestParser {
    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        // 리퀘스트 라인 파싱
        String[] requestLineParts = extractRequestLineParts(br);
        String queryString = URLDecoder.decode(requestLineParts[1], StandardCharsets.UTF_8);
        RequestLine requestLine = getRequestLine(requestLineParts, queryString);

        // 헤더 파싱
        Headers headers = new Headers(extractHeaders(br));

        // 본문 파싱
        Body body = new Body(extractBody(br, headers).getBytes());

        // 파람 파싱
        Parameters parameters = new Parameters(extractParams(queryString, headers, body));

        return new HttpRequest(requestLine, headers, body, parameters);
    }

    private static RequestLine getRequestLine(String[] requestLineParts, String queryString) {
        String method = requestLineParts[0];
        String URL = extractURL(queryString);
        return new RequestLine(method, URL);
    }

    private static String[] extractRequestLineParts(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        return requestLine.split(START_LINE_DELIMITER);
    }

    private static String extractURL(String queryString) {
        if (queryString.contains(QUERY_COMMAND_START)) {
            return queryString.substring(0, queryString.indexOf(QUERY_COMMAND_START));
        }
        return queryString;
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

    private static String extractBody(BufferedReader br, Headers headers) throws IOException {
        if (headers.containsKey(HttpConst.HEADER_CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(headers.get(HttpConst.HEADER_CONTENT_LENGTH));
            char[] body = new char[contentLength];
            br.read(body);
            return new String(body);
        }
        return HttpConst.EMPTY_STRING;
    }

    private static Map<String, String> extractParams(String url, Headers headers, Body body) {
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

    private static Map<String, String> extractBodyParams(Headers headers, Body body) {
        Map<String, String> params = new HashMap<>();
        String contentType = headers.get(HttpConst.HEADER_CONTENT_TYPE);
        if (contentType != null && contentType.equals(HTML_FORM_CONTENT_TYPE)) {
            String decodedBody = URLDecoder.decode(body.getStringContent(), StandardCharsets.UTF_8);
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
