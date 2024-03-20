package response;

import request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {
    private final String RELATIVE_PATH = "./src/main/resources/static";
    private final HttpRequest httpRequest;
    public ResponseHandler(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public void sendResponseDependOnRequest(DataOutputStream dos) throws IOException {
        HttpResponse httpResponse = new HttpResponse();
        String requestMethod = httpRequest.getRequestMethod();
        String requestPath = httpRequest.getRequestPath();

        if (requestMethod.equals("GET")) {
            httpResponse.respondHtmlFile(dos,RELATIVE_PATH + requestPath, httpRequest.getMimeType());
        }
        if (requestMethod.equals("POST") && (requestPath.equals("/create"))){
            httpResponse.handleRegistrationRequest(dos,httpRequest.getRequestBody());
        }
    }
}
