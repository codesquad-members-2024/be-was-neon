package http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private HttpMethod method;
    private String requestURI = "";
    private String httpVersion = "";
    private String host = "";
    private String userAgent = "";
    private String accept = "";
    private String acceptLanguage = "";
    private String acceptEncoding = "";
    private String referer = "";
    private String connection = "";
    private String ifModifiedSince = "";
    private String ifNoneMatch = "";
    private String cacheControl = "";
    private String cookie = "";
    private String pragma = "";
    private Map<String, String> parameter = new HashMap<>();

    public HttpRequest build() {
        return new HttpRequest(
                method, requestURI, httpVersion, host, userAgent, accept, acceptLanguage, acceptEncoding,
                referer, connection, ifModifiedSince, ifNoneMatch, cacheControl, cookie, pragma, parameter
        );
    }

    public HttpRequestBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpRequestBuilder setRequestURI(String requestURI) {
        this.requestURI = requestURI;
        return this;
    }

    public HttpRequestBuilder setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        return this;
    }

    public HttpRequestBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public HttpRequestBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public HttpRequestBuilder setAccept(String accept) {
        this.accept = accept;
        return this;
    }

    public HttpRequestBuilder setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
        return this;
    }

    public HttpRequestBuilder setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
        return this;
    }

    public HttpRequestBuilder setReferer(String referer) {
        this.referer = referer;
        return this;
    }

    public HttpRequestBuilder setConnection(String connection) {
        this.connection = connection;
        return this;
    }

    public HttpRequestBuilder setIfModifiedSince(String ifModifiedSince) {
        this.ifModifiedSince = ifModifiedSince;
        return this;
    }

    public HttpRequestBuilder setIfNoneMatch(String ifNoneMatch) {
        this.ifNoneMatch = ifNoneMatch;
        return this;
    }

    public HttpRequestBuilder setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public HttpRequestBuilder setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public HttpRequestBuilder setPragma(String pragma) {
        this.pragma = pragma;
        return this;
    }

    public HttpRequestBuilder setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
        return this;
    }
}
