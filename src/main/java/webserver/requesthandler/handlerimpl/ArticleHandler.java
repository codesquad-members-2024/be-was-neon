package webserver.requesthandler.handlerimpl;

import java.io.IOException;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.requesthandler.url.URLConst;

public class ArticleHandler implements RequestHandler {
    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        setHTMLToBody(response, URLConst.ARTICLE_URL);
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) {
        response.setNotFound();
    }
}
