package webserver.requesthandler.authenticator;

import java.io.IOException;
import model.User;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;
import webserver.requesthandler.session.SessionManager;

public class Authenticator {
    private final UnauthenticatedURLs unauthenticatedURLs; // 로그인이 필요 없는 경로 모음

    public Authenticator(UnauthenticatedURLs unAuthenticatedURLs) {
        this.unauthenticatedURLs = unAuthenticatedURLs;
    }

    public boolean isAuthenticated(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getPath();
        if (!unauthenticatedURLs.isLoginCheckPath(requestPath)) { // 로그인이 필요하지 않은 경로면 검사 X
            return true;
        }

        // 로그인이 필요할때 로직
        boolean isLoggedIn = isLoggedIn(request);
        if (isLoggedIn) { // 로그인이 되어 있으면 true 리턴
            return true;
        }

        response.setRedirect("/login?redirectURL=" + requestPath); // 로그인이 되어있지 않으면 리다이렉트 설정
        return false;
    }

    private boolean isLoggedIn(HttpRequest request) {
        // 로그인 상태 확인
        User user = (User) SessionManager.findSession(request);
        return user != null;
    }
}
