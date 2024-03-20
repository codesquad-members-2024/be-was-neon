package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.io.File;

import static webserver.handler.Path.*;

public class RegistrationHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        File file = new File(REGISTRATION_PAGE.getRelativePath());

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.OK);
        response.setBody(file);
        return response;
    }
}
