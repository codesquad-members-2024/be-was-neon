package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import session.SessionManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ResponseHandler {
    private final String RELATIVE_PATH = "./src/main/resources/static";
    private static final String MAIN_PAGE_URL = "/index.html";
    private static final String LOGIN_FAIL_PAGE_URL = "/login/login_failed.html";
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private final HttpRequest httpRequest;
    public ResponseHandler(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public void sendResponseDependOnRequest(DataOutputStream dos) throws IOException {
        HttpResponse httpResponse = new HttpResponse();

        if (httpRequest.getMethod().equals("GET")) {
            responseWhenMethodIsGet(dos,httpResponse);
        } else if (httpRequest.getMethod().equals("POST")) {
            if (httpRequest.getPath().startsWith("/create")) {
                httpResponse.handleRegistrationRequest(dos, httpRequest.getRequestBody(),MAIN_PAGE_URL);
            }
        }
    }

    public void responseWhenMethodIsGet(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        if (httpRequest.getPath().startsWith("/user")) {
            String sessionId = SessionManager.createSession(httpRequest.getQueryParam());
            if (!sessionId.isEmpty()){
                httpResponse.handleLoginRequest(dos,sessionId, MAIN_PAGE_URL);
                logger.debug("Login Completed");
            }else{
                httpResponse.handleLoginRequest(dos,sessionId, LOGIN_FAIL_PAGE_URL);
                logger.debug("Login Failed");
            }
        } else {
            httpResponse.respondToHtmlFile(dos,RELATIVE_PATH + httpRequest.getPath(), httpRequest.getMimeType());
        }
    }
}
