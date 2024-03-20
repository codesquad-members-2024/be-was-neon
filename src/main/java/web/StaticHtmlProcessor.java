package web;

import http.HttpRequest;
import http.HttpResponse;

public class StaticHtmlProcessor extends HttpProcessor {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        byte[] resource = getBytes(request);

        responseHeader200(response, getContentType(request));
        responseMessage(response, resource);

        response.flush();
    }
}
