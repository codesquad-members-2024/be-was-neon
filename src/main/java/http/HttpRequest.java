package http;

import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;
    private final String requestURI;
    private final String httpVersion;
    private final String host;
    private final String userAgent;
    private final String accept;
    private final String acceptLanguage;
    private final String acceptEncoding;
    private final String referer;
    private final String connection;
    private final String ifModifiedSince;
    private final String ifNoneMatch;
    private final String cacheControl;
    private final String cookie;
    private final String pragma;
    private final Map<String, String> parameter;

    protected HttpRequest(HttpMethod method, String requestURI, String httpVersion, String host, String userAgent,
                       String accept, String acceptLanguage, String acceptEncoding, String referer, String connection,
                       String ifModifiedSince, String ifNoneMatch, String cacheControl, String cookie, String pragma,
                          Map<String, String> parameter) {
        this.method = method;
        this.requestURI = requestURI;
        this.httpVersion = httpVersion;
        this.host = host;
        this.userAgent = userAgent;
        this.accept = accept;
        this.acceptLanguage = acceptLanguage;
        this.acceptEncoding = acceptEncoding;
        this.referer = referer;
        this.connection = connection;
        this.ifModifiedSince = ifModifiedSince;
        this.ifNoneMatch = ifNoneMatch;
        this.cacheControl = cacheControl;
        this.cookie = cookie;
        this.pragma = pragma;
        this.parameter = parameter;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHost() {
        return host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public String getReferer() {
        return referer;
    }

    public String getConnection() {
        return connection;
    }

    public String getIfModifiedSince() {
        return ifModifiedSince;
    }

    public String getIfNoneMatch() {
        return ifNoneMatch;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public String getCookie() {
        return cookie;
    }

    public String getPragma() {
        return pragma;
    }

    public String getParameter(String parameterName) {
        return parameter.getOrDefault(parameterName, "");
    }
}
