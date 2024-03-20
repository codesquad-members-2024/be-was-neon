package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

public interface Handler {
    HttpResponse service(HttpRequest request);
}
