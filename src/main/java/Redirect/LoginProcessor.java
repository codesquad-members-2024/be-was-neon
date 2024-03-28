package Redirect;

import webserver.HttpRequest;
import webserver.HttpResponseWriter;

import java.io.IOException;

public class LoginProcessor implements RequestProcessor{
    @Override
    public void handleFilerequest(HttpRequest request, HttpResponseWriter responseWriter) throws IOException {
        responseWriter.sendRedirect("/login/index.html");
    }
}
