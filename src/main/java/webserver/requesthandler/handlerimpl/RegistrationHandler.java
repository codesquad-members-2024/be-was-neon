package webserver.requesthandler.handlerimpl;

import db.Database;
import java.io.IOException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.requesthandler.URLConst;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;

public class RegistrationHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        setHTMLToBody(response, URLConst.REGISTRATION_URL);
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) {
        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        Database.addUser(user);
        logger.debug("신규 유저가 생성되었습니다. {}", user);
        response.setRedirect(URLConst.LOGIN_URL);
    }
}
