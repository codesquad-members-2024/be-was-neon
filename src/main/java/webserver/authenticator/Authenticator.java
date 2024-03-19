package webserver.authenticator;

import java.io.IOException;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.requesthandler.url.UnauthenticatedURLs;
import webserver.session.SessionManager;

public class Authenticator {
    private final UnauthenticatedURLs unauthenticatedURLs; // 로그인이 필요 없는 경로 모음

    public Authenticator(UnauthenticatedURLs unAuthenticatedURLs) {
        this.unauthenticatedURLs = unAuthenticatedURLs;
    }

    public boolean isAuthenticated(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getPath();

        if (unauthenticatedURLs.isLoginCheckPath(requestPath)) { // 로그인이 필요한 경로인지 검사
            boolean isLoggedIn = isLoggedIn(request);
            if (!isLoggedIn) { // 로그인이 안되어 있으면 로그인 화면으로 리다이렉트
                response.sendRedirect("/login?redirectURL=" + requestPath);
                return false;
            }
        }

        return true;
    }

    private boolean isLoggedIn(HttpRequest request) {
        // 로그인 상태 확인
        User user = (User) SessionManager.findSession(request);
        return user != null;
    }
}
