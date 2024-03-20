package webserver.requesthandler.handlerimpl;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.requesthandler.URLConst;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;
import webserver.requesthandler.http.HttpStatus;
import webserver.requesthandler.session.SessionManager;

public class LogoutHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) {
        User user = (User) SessionManager.findSession(request);

        if (user != null) {
            SessionManager.expire(request);
            logger.debug("{} 님이 로그아웃 하셨습니다.", user.getUserId());
            response.setRedirect(URLConst.HOME_URL);
        }
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND); // 잘못된 요청
    }
}
