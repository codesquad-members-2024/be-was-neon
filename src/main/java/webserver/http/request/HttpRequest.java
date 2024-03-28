package webserver.http.request;

import webserver.http.request.message.HttpMethod;
import webserver.http.request.message.HttpRequestBody;
import webserver.http.request.message.HttpRequestHeader;
import webserver.http.request.message.HttpRequestStartLine;

public class HttpRequest {

    private HttpRequestStartLine startLine;
    private HttpRequestHeader header;
    private HttpRequestBody body;

    public HttpRequest(HttpRequestStartLine startLine, HttpRequestHeader header, HttpRequestBody body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return startLine.getHttpMethod();
    }

    public HttpRequestHeader getRequestHeader() {
        return header;
    }

    public HttpRequestBody getRequestBody() {
        return body;
    }

    public String getRequestLine() {
        return startLine.getRequestLine();
    }

    public String getHttpVersion() {
        return startLine.getHttpVersion();
    }

    public String getHeaderValue(String key) {
        return header.getValue(key);
    }

    public String getBody() {
        return body.getBody();
    }

}
