package webserver.requesthandler.http;

import java.net.HttpCookie;
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
        headers = new HashMap<>();
        params = new HashMap<>();
    }


    public boolean isGET() {
        return this.method.equals(HttpConst.METHOD_GET);
    }

    public boolean isPOST() {
        return this.method.equals(HttpConst.METHOD_POST);
    }

    public HttpCookie getCookie(String cookieName) {
        String cookieValues = headers.get(HttpConst.HEADER_COOKIE);
        if (cookieValues == null) {
            return null;
        }
        String[] cookieParts = cookieValues.split(HttpConst.COOKIE_VALUE_DELIMITER + HttpConst.SPACE_REGEX);
        return Arrays.stream(cookieParts)
                .map(cookiePart -> HttpCookie.parse(cookiePart).get(0))
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst()
                .orElse(null);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
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

    public void setMethod(String method) {
        this.method = method;
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

    public void setBody(String body) {
        this.body = body;
    }
}
