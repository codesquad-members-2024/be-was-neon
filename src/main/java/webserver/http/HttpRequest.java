package webserver.http;

import static webserver.http.HttpConst.CONTENT_LENGTH_LABEL;
import static webserver.http.HttpConst.CONTENT_TYPE_LABEL;
import static webserver.http.HttpConst.COOKIE_VALUE_DELIMITER;
import static webserver.http.HttpConst.EMPTY_STRING;
import static webserver.http.HttpConst.HEADER_DELIMITER;
import static webserver.http.HttpConst.HTML_FORM_CONTENT_TYPE;
import static webserver.http.HttpConst.PARAMS_DELIMITER;
import static webserver.http.HttpConst.PARAMS_LENGTH;
import static webserver.http.HttpConst.PARAM_DELIMITER;
import static webserver.http.HttpConst.QUERY_COMMAND_START;
import static webserver.http.HttpConst.SPACE_REGEX;
import static webserver.http.HttpConst.START_LINE_DELIMITER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers;
    private String body;
    private Map<String, String> params;

    public HttpRequest() {
    }

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] requestLineParts = extractRequestLineParts(br);
        this.method = requestLineParts[0];
        String url = URLDecoder.decode(requestLineParts[1], StandardCharsets.UTF_8);
        this.path = extractPath(url);
        this.headers = extractHeaders(br);
        this.body = extractBody(br);
        this.params = extractParams(url);
    }


    public HttpCookie getCookie(String cookieName) {
        String cookieValues = headers.get("Cookie");
        if (cookieValues == null) {
            return null;
        }
        String[] cookieParts = cookieValues.split(COOKIE_VALUE_DELIMITER + SPACE_REGEX);
        return Arrays.stream(cookieParts)
                .map(cookiePart -> HttpCookie.parse(cookiePart).get(0))
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    public boolean isGET() {
        return this.method.equals("GET");
    }

    public boolean isPOST() {
        return this.method.equals("POST");
    }

    public String getHeader(String headerKey) {
        return headers.get(headerKey);
    }

    public String getParameter(String paramKey) {
        return params.get(paramKey);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    private String[] extractRequestLineParts(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        return requestLine.split(START_LINE_DELIMITER);
    }

    private String extractPath(String url) {
        if (url.contains(QUERY_COMMAND_START)) {
            return url.substring(0, url.indexOf(QUERY_COMMAND_START));
        }
        return url;
    }


    private Map<String, String> extractHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(HEADER_DELIMITER);
            headers.put(headerParts[0].trim(), headerParts[1].trim());
        }
        return headers;
    }

    private String extractBody(BufferedReader br) throws IOException {
        if (headers.containsKey(CONTENT_LENGTH_LABEL)) {
            int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH_LABEL));
            char[] body = new char[contentLength];
            br.read(body);
            return new String(body);
        }
        return EMPTY_STRING;
    }

    private Map<String, String> extractParams(String url) {
        Map<String, String> params = extractQueryParams(url);
        params.putAll(extractBodyParams());
        return params;
    }

    private Map<String, String> extractQueryParams(String url) {
        Map<String, String> params = new HashMap<>();
        if (url.contains(QUERY_COMMAND_START)) {
            String[] paramParts = url.substring(url.indexOf(QUERY_COMMAND_START) + 1).split(PARAMS_DELIMITER);
            addParams(paramParts, params);
        }
        return params;
    }

    private Map<String, String> extractBodyParams() {
        Map<String, String> params = new HashMap<>();
        String contentType = headers.get(CONTENT_TYPE_LABEL);
        if (contentType != null && contentType.equals(HTML_FORM_CONTENT_TYPE)) {
            String decoded = URLDecoder.decode(body, StandardCharsets.UTF_8);
            String[] paramParts = decoded.split(PARAMS_DELIMITER);
            addParams(paramParts, params);
        }
        return params;
    }

    private void addParams(String[] paramParts, Map<String, String> params) {
        for (String param : paramParts) {
            String[] splitedParam = param.split(PARAM_DELIMITER);
            if (splitedParam.length == PARAMS_LENGTH) {
                params.put(splitedParam[0], splitedParam[1]);
            }
        }
    }
}
