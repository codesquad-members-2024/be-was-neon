package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.File;

import static webserver.handler.Path.*;

public class RegistrationHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        File file = new File(REGISTRATION_PAGE.getRelativePath());
        return HttpResponse.from(file);
    }
}
