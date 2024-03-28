package webserver;

import Utils.FileUtils;
import Utils.RouteManager;

import java.io.IOException;

public class HttpResponse {
    private final HttpResponseWriter out;
    private final byte[] body;
    private final String contentType;

    public HttpResponse(HttpResponseWriter out, String filePath) throws IOException {
        this.out = out;
        this.body = FileUtils.readFileContent(filePath);
        this.contentType = RouteManager.getContentType(filePath);
        sendResponse();
    }

    private void sendResponse() throws IOException {
        out.sendResponse(body, contentType);
    }
}
