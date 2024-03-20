package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.utils.HttpRequestParser;

import java.util.Map;

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
        if (user == null) {
            logger.info("존재하지 않는 ID");
            return HttpResponse.redirect(LOGIN_FAILED_PAGE.getPath());
        }
        if (!user.getPassword().equals(password)) {
            logger.info("비밀번호 불일치 {}, {}", user.getPassword(), password);
            return HttpResponse.redirect(LOGIN_FAILED_PAGE.getPath());
        }

        logger.info("{} : 로그인 성공", user.getUserId());
        return HttpResponse.redirect(HOME_PAGE.getPath());
    }
}
