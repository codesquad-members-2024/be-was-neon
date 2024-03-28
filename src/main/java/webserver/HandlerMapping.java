package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.requesthandler.*;

import java.io.IOException;

public class HandlerMapping {

    public final static String REGISTRATION_URL = "/registration";
    public final static String LOGIN_URL = "/login";
    public final static String LOGOUT_URL = "/logout";

    private static final Logger logger = LoggerFactory.getLogger(HandlerMapping.class);

    public Handler findHandler(HttpRequest httpRequest) throws IOException {
        return createHandler(httpRequest);
    }


    private Handler createHandler(HttpRequest httpRequest) {
        // 요청 대상에 따라 적절한 핸들러 생성
        if (REGISTRATION_URL.equals(httpRequest.getRequestTarget())) {
            return new RegistrationHandler();
        } else if (LOGIN_URL.equals(httpRequest.getRequestTarget())){
            return new LoginHandler();
        } else if (LOGOUT_URL.equals(httpRequest.getRequestTarget())) {
            return new LogOutHandler();
        }else {
            return new StaticHandler();
        }
    }
}