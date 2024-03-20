package webserver.requesthandler.handlerMapper;

import static webserver.requesthandler.URLConst.ARTICLE_URL;
import static webserver.requesthandler.URLConst.COMMENT_URL;
import static webserver.requesthandler.URLConst.HOME_URL;
import static webserver.requesthandler.URLConst.LOGIN_URL;
import static webserver.requesthandler.URLConst.LOGOUT_URL;
import static webserver.requesthandler.URLConst.REGISTRATION_URL;
import static webserver.requesthandler.URLConst.USER_LIST_URL;

import java.util.HashMap;
import java.util.Map;
import webserver.requesthandler.handlerimpl.ArticleHandler;
import webserver.requesthandler.handlerimpl.CommentHandler;
import webserver.requesthandler.handlerimpl.HomeHandler;
import webserver.requesthandler.handlerimpl.LoginHandler;
import webserver.requesthandler.handlerimpl.LogoutHandler;
import webserver.requesthandler.handlerimpl.RegistrationHandler;
import webserver.requesthandler.handlerimpl.RequestHandler;
import webserver.requesthandler.handlerimpl.StaticResourceHandler;
import webserver.requesthandler.handlerimpl.UserListHandler;
import webserver.requesthandler.http.HttpRequest;

public class RequestHandlerMapper {
    private static final Map<String, RequestHandler> store = new HashMap<>();

    public RequestHandlerMapper() {
        store.put(HOME_URL, new HomeHandler());
        store.put(REGISTRATION_URL, new RegistrationHandler());
        store.put(LOGIN_URL, new LoginHandler());
        store.put(LOGOUT_URL, new LogoutHandler());
        store.put(ARTICLE_URL, new ArticleHandler());
        store.put(COMMENT_URL, new CommentHandler());
        store.put(USER_LIST_URL, new UserListHandler());
    }

    public RequestHandler findRequestHandler(HttpRequest request) {
        return store.getOrDefault(request.getPath(), new StaticResourceHandler());
    }
}
