package webserver.requesthandler.handlerimpl;

import static webserver.requesthandler.URLConst.INDEX_HTML_FILE;
import static webserver.requesthandler.URLConst.STATIC_DIRECTORY;

import java.io.IOException;
import java.util.Objects;
import webserver.requesthandler.http.ContentType;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;

public interface RequestHandler {
    default void handle(HttpRequest request, HttpResponse response) throws IOException {
        if (request.isGET()) {
            handleGet(request, response);
        }
        if (request.isPOST()) {
            handlePost(request, response);
        }
    }

    void handleGet(HttpRequest request, HttpResponse response) throws IOException;

    void handlePost(HttpRequest request, HttpResponse response) throws IOException;

    default void setHTMLToBody(HttpResponse response, String resourcePath) throws IOException {
        byte[] body = Objects.requireNonNull(
                Objects.requireNonNull(
                                getClass().getResourceAsStream(STATIC_DIRECTORY + resourcePath + INDEX_HTML_FILE))
                        .readAllBytes());
        response.setBody(body, ContentType.HTML);
    }
}
