package webserver.requesthandler.http.message;

import webserver.requesthandler.http.HttpConst;

public class RequestLine {
    private String method;
    private String URL;

    public RequestLine(String method, String URL) {
        this.method = method;
        this.URL = URL;
    }

    public RequestLine() {
    }

    public boolean isGET() {
        return method.equals(HttpConst.METHOD_GET);
    }

    public boolean isPOST() {
        return method.equals(HttpConst.METHOD_POST);
    }

    public String getMethod() {
        return method;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
