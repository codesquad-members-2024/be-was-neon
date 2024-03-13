package web;

import static utils.ResourceHandler.BASE_NAME;
import static utils.ResourceHandler.HTML_EXTENSION;
import static utils.ResourceHandler.RESOURCE_PATH;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import utils.ResourceHandler;

public class HtmlProcessor extends HttpProcessor {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        byte[] html = read(request);

        responseHeader200(response);
        responseMessage(response, html);

        response.flush();
    }

    private byte[] read(HttpRequest request) {
        if (request.getRequestURI().endsWith(HTML_EXTENSION)) {
            return ResourceHandler.read(RESOURCE_PATH + request.getRequestURI());
        }
        return ResourceHandler.read(RESOURCE_PATH + request.getRequestURI() + BASE_NAME + HTML_EXTENSION);
    }

    private void responseHeader200(HttpResponse response) {
        response.setHttpVersion("HTTP/1.1")
                .setStatusCode(HttpStatus.STATUS_OK)
                .setContentType("text/html")
                .setCharset("utf-8");
    }

    private void responseMessage(HttpResponse response, byte[] html) {
        response.setContentLength(html.length)
                .setMessageBody(html);
    }
}
