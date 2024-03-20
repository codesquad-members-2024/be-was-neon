package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        HttpResponse response = new HttpResponse();
        if (user == null) {
            logger.info("존재하지 않는 ID");
            response.redirect(LOGIN_FAILED_PAGE.getPath());
            return response;
        }
        if (!user.getPassword().equals(password)) {
            logger.info("비밀번호 불일치 {}, {}", user.getPassword(), password);
            response.redirect(LOGIN_FAILED_PAGE.getPath());
            return response;
        }
        logger.info("{} : 로그인 성공", user.getUserId());

        response.redirect(HOME_PAGE.getPath());
        UUID uuid = UUID.randomUUID();
        response.setCookie(uuid);
        return response;
    }
}
