package request;

import java.util.Map;

public class HttpRequest {
    private RequestLine requestLine;
    private Map<String, String> requestHeader;
    private String requestBody;
    private String cookie;

    public HttpRequest(RequestLine requestLine, Map<String,String> requestHeader,String requestBody){
        this.requestLine = requestLine;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        setCookie();

    }
    public void setCookie(){
        cookie = requestHeader.get("Cookie");
    }
    public RequestLine getRequestLine(){
        return requestLine;
    }
    public String getRequestMethod() {
        return requestLine.getRequestMethod();
    }
    public String getRequestPath() {
        return requestLine.getRequestPath();
    }
    public String getRequestProtocol() {
        return requestLine.getRequestProtocol();
    }
    public String getMimeType() {
        return requestLine.getMimeType();
    }
    public Map<String,String> getRequestHeader() {
        return requestHeader;
    }
    public String getRequestBody() {
        return requestBody;
    }
    public String getCookie() {
        return cookie;
    }
}
