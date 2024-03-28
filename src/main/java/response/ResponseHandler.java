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

        // 로그인인 경우
        if (httpRequest.getPath().startsWith("/loginAction")) {
            String sessionId = "";
            Map<String, String> UserData = UserDataParser.extractUserData(httpRequest.getQueryParam());
            // Database 에 있는 유저와 아이디 비번이 같은 경우 sessionId 를 반환한다.
            if (loginHandler.isLoginDataValid(UserData)){
                sessionId = SessionManager.createSession(loginHandler.getLoginUser());
            }
            // 세션 아이디가 있는 경우 로그인 성공
            if (!sessionId.isEmpty()){
                httpResponse = getResponseHandler.respondToLogin(httpRequest,sessionId, LOGIN_HOME_PAGE.getPath());
                logger.debug("Login Completed");
            }
            // 세션 아이디가 비어있는 경우 로그인 실패
            else{
                httpResponse = getResponseHandler.respondToLogin(httpRequest,sessionId + ";Max-Age=0", LOGIN_FAILED_PAGE.getPath());
                logger.debug("Login Failed");
            }
        }
        // 다른 모든 경우
        else{
            httpResponse = getResponseHandler.respondToHtmlFile(httpRequest);
        }
    }
    private void responseToPostMethod() throws IOException{
        PostResponseHandler postResponseHandler = new PostResponseHandler();
        RegistrationHandler registrationHandler = new RegistrationHandler();

        // 회원가입인 경우
        if (httpRequest.getPath().startsWith("/registrationAction")) {
            registrationHandler.registerNewUser(UserDataParser.extractUserData(httpRequest.getRequestBody()));
            httpResponse = postResponseHandler.respondToRegistration(httpRequest, HOME_PAGE.getPath());
        }
        // 로그아웃인 경우
        else if(httpRequest.getPath().startsWith("/logoutAction")){
            SessionManager.removeSession(httpRequest.getSessionId());
            httpResponse = postResponseHandler.respondToLogout(httpRequest, HOME_PAGE.getPath());
        }
    }
}
