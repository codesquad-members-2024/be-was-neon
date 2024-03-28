package response;

import enums.FilePath;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import session.SessionManager;
import userservice.LoginHandler;
import userservice.RegistrationHandler;
import userservice.UserDataParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static enums.FilePath.*;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private final HttpRequest httpRequest;
    HttpResponse httpResponse;

    public ResponseHandler(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public void sendResponseDependOnRequest(DataOutputStream dos) throws IOException {
        if (httpRequest.getMethod().equals("GET")) {
            responseToGetMethod();
        } else if (httpRequest.getMethod().equals("POST")) {
            responseToPostMethod();
        }
        httpResponse.sendResponse(dos);
    }

    private void responseToGetMethod() throws IOException {
        GetResponseHandler getResponseHandler = new GetResponseHandler();
        LoginHandler loginHandler = new LoginHandler();

        if (httpRequest.getPath().startsWith("/loginAction")) {
            String sessionId = "";
            Map<String, String> UserData = UserDataParser.extractUserData(httpRequest.getQueryParam());
            if (loginHandler.isLoginDataValid(UserData)){
                sessionId = SessionManager.createSession(loginHandler.getLoginUser());
            }

            if (!sessionId.isEmpty()){
                httpResponse = getResponseHandler.respondToLogin(httpRequest,sessionId, LOGIN_HOME_PAGE.getPath());
                logger.debug("Login Completed");
            }else{
                httpResponse = getResponseHandler.respondToLogin(httpRequest,sessionId + ";Max-Age=0", LOGIN_FAILED_PAGE.getPath());
                logger.debug("Login Failed");
            }
        } else{
            httpResponse = getResponseHandler.respondToHtmlFile(httpRequest);
        }
    }
    private void responseToPostMethod() throws IOException{
        PostResponseHandler postResponseHandler = new PostResponseHandler();
        RegistrationHandler registrationHandler = new RegistrationHandler();

        if (httpRequest.getPath().startsWith("/registrationAction")) {
            registrationHandler.registerNewUser(UserDataParser.extractUserData(httpRequest.getRequestBody()));
            httpResponse = postResponseHandler.respondToRegistration(httpRequest, HOME_PAGE.getPath());

        }else if(httpRequest.getPath().startsWith("/logoutAction")){
            SessionManager.removeSession(httpRequest.getSessionId());
            httpResponse = postResponseHandler.respondToLogout(httpRequest, HOME_PAGE.getPath());
        }

    }
}
