package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {
    private final String RELATIVE_PATH = "./src/main/resources/static";
    private final HttpRequest httpRequest;
    ResponseHandler(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public void sendResponseDependOnRequest(DataOutputStream dos) throws IOException {
        HttpResponse httpResponse = new HttpResponse();
        RegistrationHandler registrationHandler = new RegistrationHandler();
        String requestPath = httpRequest.getRequestPath();
        if (httpRequest.getRequestMethod().equals("GET")) {
            httpResponse.respondHtmlFile(dos,RELATIVE_PATH + requestPath,httpRequest.getContentType());
        }else if (httpRequest.getRequestMethod().equals("GET") && (httpRequest.getRequestPath().equals("/login"))){
            httpResponse.handleRegistrationRequest(dos,httpRequest.getRequestBody());
        }

        if (httpRequest.getRequestMethod().equals("POST") && (httpRequest.getRequestPath().equals("/create"))){
            httpResponse.handleRegistrationRequest(dos,httpRequest.getRequestBody());
        }
    }
}
