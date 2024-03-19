package webserver.requesthandler.handlerimpl;

import db.Database;
import java.io.IOException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.requesthandler.url.URLConst;
import webserver.session.SessionManager;

public class LoginHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        setHTMLToBody(response, URLConst.LOGIN_URL); // 로그인폼 렌더링
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User findUser = Database.findUserById(userId);
        if (findUser == null) { // 일치하는 Id가 없을 경우
            response.setRedirect(URLConst.LOGIN_URL);
            return;
        }
        if (!findUser.hasSamePassword(password)) { // 일치하는 Id는 있으나 비밀번호가 다를 경우
            response.setRedirect(URLConst.LOGIN_URL);
            return;
        }
        SessionManager.createSession(findUser, response);
        String redirectURL = request.getParameter("redirectURL");

        setRedirectURL(response, redirectURL);
        logger.debug("{} 님이 로그인 하셨습니다.", userId);
    }

    private void setRedirectURL(HttpResponse response, String redirectURL) {
        if (redirectURL == null) { // 널이면 홈화면으로, 요청 경로가 있다면 해당 경로로 리다이렉트
            response.setRedirect(URLConst.HOME_URL);
            return;
        }
        response.setRedirect(redirectURL);
    }
}
