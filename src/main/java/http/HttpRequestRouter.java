package http;

import static utils.Constant.CONTENT_TYPE_DELIMITER;

import http.request.FilePath;
import http.request.HttpRequest;
import http.request.Method;
import http.request.Method.HttpMethod;
import http.response.ContentType;
import http.response.HttpResponse;
import http.response.Status;
import java.io.File;

public class HttpRequestRouter {
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public HttpRequestRouter(HttpRequest httpRequest, HttpResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public void processRequest() throws IllegalArgumentException {
        Method method = httpRequest.getMethod();
        String methodCommand = method.getMethod().toUpperCase();
        try {
            HttpMethod.valueOf(methodCommand).execute(this);
        } catch (IllegalArgumentException e) {
            httpResponse.setErrorResponse(Status.BAD_REQUEST);
            throw e;
        }
    }

    public void processGETMethod() {
        FilePath filePath = httpRequest.getFilePath();
        File file = new File(filePath.getFilePath());
        FileReader fileReader = new FileReader(file);
        ContentType contentType = getContentType(filePath);
        byte[] httpRequestBody = fileReader.readAllBytes();

        httpResponse.setOkResponse(contentType, httpRequestBody);
    }

    public void processPOSTMethod() {
        // TODO: POST 기능 구현 (user create)
    }

    public void processPUTMethod() {

    }

    public void processDELETEMethod() {

    }

    public void processHEADMethod() {

    }

    public void processCONNECTMethod() {

    }

    public void processTRACEMethod() {

    }

    public void processPATCHMethod() {

    }

    private ContentType getContentType(FilePath filePath) {
        String[] parts = filePath.getFilePath().split(CONTENT_TYPE_DELIMITER);
        String type = parts[parts.length - 1];

        for (ContentType contentType : ContentType.values()) {
            if (contentType.name().equals(type)) {
                return contentType;
            }
        }
        throw new IllegalArgumentException();
    }
}
