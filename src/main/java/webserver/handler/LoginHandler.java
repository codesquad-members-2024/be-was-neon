package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Session.SessionManger;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.utils.HttpRequestParser;

import java.util.Map;
import java.util.UUID;

import static webserver.handler.Path.HOME_PAGE;
import static webserver.handler.Path.LOGIN_FAILED_PAGE;


public class LoginHandler implements Handler{

    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public HttpResponse service(HttpRequest request) {
        String body = request.getBody();
        Map<String, String> loginQuery = HttpRequestParser.parseKeyValuePairs(body);
        String id = loginQuery.get("userId");
        String password = loginQuery.get("password");

        User user = Database.findUserById(id);
        if (canLogin(user, password)) {
            return loginSuccess(user);
        }
        return loginFailed();
    }

    private static boolean canLogin(User user, String password) {
        return user != null && user.verifyPassword(password);
    }

    private static HttpResponse loginSuccess(User user) {
        logger.info("{} : 로그인 성공", user.getUserId());

        HttpResponse response = new HttpResponse();
        UUID uuid = UUID.randomUUID();
        response.setCookie(uuid);
        SessionManger.addLoginUser(uuid.toString(), user);
        response.redirect(HOME_PAGE.getPath());
        return response;
    }

    private static HttpResponse loginFailed() {
        logger.info("로그인 실패");
        
        HttpResponse response = new HttpResponse();
        response.redirect(LOGIN_FAILED_PAGE.getPath());
        return response;
    }
}
