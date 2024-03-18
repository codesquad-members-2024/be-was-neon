package http;

import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;
    private final String requestURI;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final Map<String, String> parameter;

    protected HttpRequest(HttpMethod method, String requestURI, String httpVersion,
                          Map<String, String> headers, Map<String, String> parameter) {
        this.method = method;
        this.requestURI = requestURI;
        this.httpVersion = httpVersion;
        this.headers = headers;
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

    public String getHeader(String headerName) {
        return headers.getOrDefault(headerName, "");
    }

    public String getParameter(String parameterName) {
        return parameter.getOrDefault(parameterName, "");
    }
}
