package webserver.handler;

import config.Config;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface HttpRequestHandler {
    HttpResponse handleRequest(HttpRequest httpRequest);
}
