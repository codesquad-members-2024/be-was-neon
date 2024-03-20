package webserver.handler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;
import webserver.utils.HttpRequestParser;

import java.util.Map;

import static webserver.handler.Path.HOME_PAGE;

public class UserCreateHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(UserCreateHandler.class);

    @Override
    public HttpResponse service(HttpRequest request) {
        String body = request.getBody();
        Map<String, String> userForm = HttpRequestParser.parseKeyValuePairs(body);

        User user = User.from(userForm);
        Database.addUser(user);
        logger.debug("User created : {}", Database.findUserById(user.getUserId()));

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.SEE_OTHER);
        response.setLocation(HOME_PAGE.getPath());

        return response;
    }
}
