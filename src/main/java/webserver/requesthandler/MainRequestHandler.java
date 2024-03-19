package webserver.requesthandler;

import static webserver.requesthandler.url.URLConst.ARTICLE_URL;
import static webserver.requesthandler.url.URLConst.COMMENT_URL;
import static webserver.requesthandler.url.URLConst.HOME_URL;
import static webserver.requesthandler.url.URLConst.LOGIN_URL;
import static webserver.requesthandler.url.URLConst.LOGOUT_URL;
import static webserver.requesthandler.url.URLConst.REGISTRATION_URL;
import static webserver.requesthandler.url.URLConst.USER_LIST_URL;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.authenticator.Authenticator;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.requesthandler.handlerimpl.ArticleHandler;
import webserver.requesthandler.handlerimpl.CommentHandler;
import webserver.requesthandler.handlerimpl.HomeHandler;
import webserver.requesthandler.handlerimpl.LoginHandler;
import webserver.requesthandler.handlerimpl.LogoutHandler;
import webserver.requesthandler.handlerimpl.RegistrationHandler;
import webserver.requesthandler.handlerimpl.RequestHandler;
import webserver.requesthandler.handlerimpl.StaticResourceHandler;
import webserver.requesthandler.handlerimpl.UserListHandler;
import webserver.requesthandler.url.UnauthenticatedURLs;

public class MainRequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    private final Socket connection;
    private final Map<String, RequestHandler> requestHandlers;

    public MainRequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        requestHandlers = initRequestHandlers();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            Authenticator authenticator = new Authenticator(new UnauthenticatedURLs());
            boolean isAuthenticated = authenticator.isAuthenticated(request, response);// 인증이 필요한 페이지에 접근하는지 확인

            if (!isAuthenticated) {
                return;
            }
            RequestHandler requestHandler = findRequestHandler(request);
            handleMethod(request, response, requestHandler);
            response.send();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, RequestHandler> initRequestHandlers() {
        Map<String, RequestHandler> requestHandlers = new HashMap<>();
        requestHandlers.put(HOME_URL, new HomeHandler());
        requestHandlers.put(REGISTRATION_URL, new RegistrationHandler());
        requestHandlers.put(LOGIN_URL, new LoginHandler());
        requestHandlers.put(LOGOUT_URL, new LogoutHandler());
        requestHandlers.put(ARTICLE_URL, new ArticleHandler());
        requestHandlers.put(COMMENT_URL, new CommentHandler());
        requestHandlers.put(USER_LIST_URL, new UserListHandler());
        return requestHandlers;
    }

    private RequestHandler findRequestHandler(HttpRequest request) {
        return requestHandlers.getOrDefault(request.getPath(), new StaticResourceHandler());
    }

    private void handleMethod(HttpRequest request, HttpResponse response, RequestHandler requestHandler)
            throws IOException {
        if (request.isGET()) {
            requestHandler.handleGet(request, response);
        }
        if (request.isPOST()) {
            requestHandler.handlePost(request, response);
        }
    }
}
