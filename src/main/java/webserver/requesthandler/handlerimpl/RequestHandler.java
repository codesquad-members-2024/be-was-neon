package webserver.requesthandler.handlerimpl;

import static webserver.requesthandler.url.URLConst.INDEX_HTML_FILE;
import static webserver.requesthandler.url.URLConst.STATIC_DIRECTORY;

import java.io.IOException;
import java.util.Objects;
import webserver.http.ContentType;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface RequestHandler {
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
