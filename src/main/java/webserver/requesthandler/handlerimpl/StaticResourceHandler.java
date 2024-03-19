package webserver.requesthandler.handlerimpl;

import java.io.IOException;
import java.io.InputStream;
import utils.FileManager;
import webserver.http.ContentType;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.requesthandler.url.URLConst;

public class StaticResourceHandler implements RequestHandler {

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getPath();

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
