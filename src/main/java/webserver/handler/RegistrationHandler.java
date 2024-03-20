package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.File;

import static webserver.handler.Path.*;

public class RegistrationHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        File file = new File(STATIC_PATH.path + REGISTRATION.path + INDEX_HTML.path);
        return HttpResponse.from(file);
    }
}
