package web;

import http.HttpRequest;
import http.HttpResponse;

public abstract class HttpProcessor {

    abstract void process(HttpRequest request, HttpResponse response);

    public void service (HttpRequest request, HttpResponse response) {
        process(request, response);
    }
}
