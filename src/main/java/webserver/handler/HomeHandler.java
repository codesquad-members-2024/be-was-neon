package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.io.File;

import static webserver.handler.Path.HOME_PAGE;


public class HomeHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        File file = new File(HOME_PAGE.getRelativePath());

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.OK);
        response.setBody(file);
        return response;
    }
}
