package model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import webserver.HttpRequestBuilder;

public class HttpRequest {
    private String httpMethod;
    private String path;
    private Map<String, String> queryString;
    private String httpVersion;
    private HttpBody httpBody;
    private int contentLength;
    public HttpRequest(HttpRequestBuilder httpRequestBuilder) throws IOException {
        this.httpMethod = httpRequestBuilder.getHttpMethod();
        this.path = httpRequestBuilder.getPath();
        this.queryString = httpRequestBuilder.getQueryString();
        this.httpVersion = httpRequestBuilder.getHttpVersion();
        if (this.httpMethod.equals("POST")){
            this.httpBody = httpRequestBuilder.getHttpBody();
        }
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public HttpBody getHttpBody() {
        return httpBody;
    }


}
