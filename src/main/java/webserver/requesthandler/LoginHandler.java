package webserver.requesthandler;

import db.Database;
import java.io.IOException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.SessionManager;

public class LoginHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    public static final String LOGIN_FORM_URL = "/login";

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User findUser = Database.findUserById(userId);
        if (findUser == null) {
            response.setRedirect(LOGIN_FORM_URL);
            return;
        }
        if (!findUser.hasSamePassword(password)) {
            response.setRedirect(LOGIN_FORM_URL);
            return;
        }
        SessionManager.createSession(findUser, response);
        response.setRedirect(MainRequestHandler.HOME_URL);
        logger.debug("{} 님이 로그인 하셨습니다.", userId);
    }
}
