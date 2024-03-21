package webserver.router;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

public interface Router {
    public HttpResponse route(HttpRequest httpRequest);
}
