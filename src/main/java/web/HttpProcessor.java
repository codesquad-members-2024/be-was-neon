package web;

import http.HttpRequest;
import http.HttpResponse;

public abstract class HttpProcessor {
    public static final String BASIC_HTTP_VERSION = "HTTP/1.1";
    public static final String BASIC_CHAR_SET = "utf-8";

    abstract void process(HttpRequest request, HttpResponse response);

    public void service (HttpRequest request, HttpResponse response) {
        process(request, response);
    }
}
