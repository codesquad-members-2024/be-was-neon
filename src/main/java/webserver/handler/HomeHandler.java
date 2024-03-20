package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.File;

import static webserver.handler.Path.HOME_PAGE;


public class HomeHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        File file = new File(HOME_PAGE.getRelativePath());
        return HttpResponse.from(file);
    }
}
