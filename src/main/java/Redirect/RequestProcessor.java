package Redirect;

import webserver.HttpRequest;
import webserver.HttpResponseWriter;

import java.io.IOException;

public interface RequestProcessor {
    void handleFilerequest(HttpRequest request, HttpResponseWriter responseWriter) throws IOException;
}

