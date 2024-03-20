package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.File;

import static webserver.handler.Path.INDEX_HTML;
import static webserver.handler.Path.STATIC_PATH;

public class StaticFileHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        String uri = request.getUri();
        File file = findFile(uri);
        return HttpResponse.from(file);
    }

    private File findFile(String uri) {
        if (isNotStaticFile(uri)) {
            uri += INDEX_HTML.path;
        }
        return new File(STATIC_PATH.path + uri);
    }

    private static boolean isNotStaticFile(String uri) {
        return !uri.contains(".");
    }
}
