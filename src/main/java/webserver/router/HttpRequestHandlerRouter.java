package webserver.router;

import webserver.handler.HttpRequestHandler;
import webserver.handler.requesthandler.*;
import webserver.http.request.HttpRequest;

public class HttpRequestHandlerRouter {
    private static final String REGISTRATION = "/registration";
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";
    private static final String USER_LIST = "/user/list";
    private static final String ROOT = "/";

    public HttpRequestHandler getRequestHandler(HttpRequest httpRequest) {
        String requestLine = httpRequest.getRequestLine();

        // 정적 요청들인 경우에는 staticFileHandler가 동작
        if (isStaticFileRequest(requestLine)) {
            return new StaticFileHandler();
        }

        // 동적 요쳥인 경우는 handler
        return switch (requestLine) {
            case ROOT -> new RootHandler();
            case REGISTRATION -> new RegistrationHandler();
            case LOGIN -> new LoginHandler();
            case LOGOUT -> new LogoutHandler();
            case USER_LIST -> new UserListHandler();
            default -> new ErrorHandler();
        };
    }

    private boolean isStaticFileRequest(String requestLine) {
        return requestLine.contains(".");
    }
}
