package web;

import static utils.ResourceHandler.*;

import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

public class StaticHtmlProcessor extends HttpProcessor {
    @Override
    public void process(HttpRequest request, HttpResponse response) {
        byte[] resource = getBytes(request);

        responseHeader200(response, getContentType(request));
        responseMessage(response, resource);

        response.flush();
    }

    private byte[] getBytes(HttpRequest request) {
        String extension = getExtension(request.getRequestURI());

        if (FILE_EXTENSION_MAP.containsKey(extension)) { // /index.html, /min.css, ...
            return read(RESOURCE_PATH + request.getRequestURI());
        }
        if (request.getRequestURI().equals("/")) { // localhost:8080/
            return read(RESOURCE_PATH + "/" + INDEX_HTML);
        }
        return read(RESOURCE_PATH + request.getRequestURI() + "/" + INDEX_HTML); // /registration
    }

    public void responseHeader200(HttpResponse response, String contentType) {
        response.setHttpVersion(BASIC_HTTP_VERSION)
                .setStatusCode(HttpStatus.STATUS_OK)
                .setContentType(contentType)
                .setCharset(BASIC_CHAR_SET);
    }

    public void responseHeader302(HttpResponse response, String location) {
        response.setHttpVersion(BASIC_HTTP_VERSION)
                .setStatusCode(HttpStatus.STATUS_FOUND)
                .setLocation(location)
                .setCharset(BASIC_CHAR_SET);
    }

    public void responseMessage(HttpResponse response, byte[] resource) {
        response.setContentLength(resource.length)
                .setMessageBody(resource);
    }

    public void responseMessage(HttpResponse response, String message) {
        response.setContentLength(message.length())
                .setMessageBody(message);
    }

    public String getContentType(HttpRequest request) {
        String extension = getExtension(request.getRequestURI());
        return FILE_EXTENSION_MAP.getOrDefault(extension, "text/html");
    }
}
