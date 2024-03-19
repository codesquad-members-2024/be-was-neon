package http;

import http.HttpRequest.HttpMethod;
import http.HttpRequest.HttpRequestUri;
import http.HttpRequest.HttpVersion;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilder {
    private HttpMethod method;
    private HttpRequestUri requestURI;
    private HttpVersion httpVersion;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameter = new HashMap<>();

    public HttpRequest build() {
        return new HttpRequest(
                method, requestURI, httpVersion, headers, parameter
        );
    }

    public HttpRequestBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpRequestBuilder setRequestURI(String requestURI) {
        this.requestURI = new HttpRequestUri(requestURI);
        return this;
    }

    public HttpRequestBuilder setHttpVersion(String httpVersion) {
        this.httpVersion = new HttpVersion(httpVersion);
        return this;
    }

    public HttpRequestBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequestBuilder setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
        return this;
    }
}
