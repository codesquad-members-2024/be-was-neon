package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.File;

import static webserver.handler.Path.INDEX_HTML;
import static webserver.handler.Path.STATIC_PATH;

public class HomeHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        File file = new File(STATIC_PATH.path + INDEX_HTML.path);
        return HttpResponse.from(file);
    }
}
