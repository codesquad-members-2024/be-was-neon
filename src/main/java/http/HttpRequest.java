package http;

public class HttpRequest {
    private HttpMethod method;
    private String requestURI;
    private String httpVersion;
    private String host;
    private String userAgent;
    private String accept;
    private String acceptLanguage;
    private String acceptEncoding;
    private String referer;
    private String connection;
    private String ifModifiedSince;
    private String ifNoneMatch;
    private String cacheControl;
    private String cookie;
    private String pragma;

    protected HttpRequest(HttpMethod method, String requestURI, String httpVersion, String host, String userAgent,
                       String accept, String acceptLanguage, String acceptEncoding, String referer, String connection,
                       String ifModifiedSince, String ifNoneMatch, String cacheControl, String cookie, String pragma) {
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
}
