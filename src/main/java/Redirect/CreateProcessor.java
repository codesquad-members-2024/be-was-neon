package Redirect;

import webserver.HttpRequest;
import webserver.HttpResponseWriter;

import java.io.IOException;

// /create의 리다이렉트를 처리하는 클래스
public class CreateProcessor implements RequestProcessor {
    @Override
    public void handleFilerequest(HttpRequest request, HttpResponseWriter responseWriter) throws IOException {
        responseWriter.sendRedirect("/index.html");
    }
}
