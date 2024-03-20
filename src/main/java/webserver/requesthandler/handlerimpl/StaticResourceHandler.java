package webserver.requesthandler.handlerimpl;

import java.io.IOException;
import java.io.InputStream;
import utils.FileManager;
import webserver.requesthandler.URLConst;
import webserver.requesthandler.http.ContentType;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;

public class StaticResourceHandler implements RequestHandler {

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getURL();

        InputStream resourceStream = getStaticResourceStream(requestPath);

        if (resourceStream == null) {
            response.setNotFound(); // 파일을 찾을 수 없는 경우 404 응답
            return;
        }

        String fileExtension = FileManager.getFileExtension(requestPath);
        byte[] body = resourceStream.readAllBytes();
        ContentType contentType = ContentType.getContentTypeByExtension(fileExtension);
        response.setBody(body, contentType);
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) throws IOException {
        response.setNotFound();
    }

    private InputStream getStaticResourceStream(String path) {
        return getClass().getResourceAsStream(URLConst.STATIC_DIRECTORY + path);
    }
}
