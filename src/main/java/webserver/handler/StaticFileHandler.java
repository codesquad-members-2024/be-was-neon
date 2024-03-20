package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.io.File;

import static webserver.handler.Path.RESOURCE_PATH;

public class StaticFileHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        String uri = request.getUri();
        File file = new File(RESOURCE_PATH.getPath() + uri);

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.OK);
        response.setBody(file);

        return response;
    }

}
