package Redirect;

import webserver.HttpRequest;
import webserver.HttpResponseWriter;

import java.io.IOException;

public class RegistProcessor implements RequestProcessor{
    @Override
    public void handleFilerequest(HttpRequest request, HttpResponseWriter responseWriter) throws IOException {
        responseWriter.sendRedirect("/registration/index.html");
    }
}
