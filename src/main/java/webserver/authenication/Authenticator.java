package webserver.authenication;

import static webserver.requesthandler.URLConst.CSS_FILE_URL;
import static webserver.requesthandler.URLConst.HOME_URL;
import static webserver.requesthandler.URLConst.IMG_FILE_URL;
import static webserver.requesthandler.URLConst.LOGIN_URL;
import static webserver.requesthandler.URLConst.REGISTRATION_URL;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.SessionManager;

public class Authenticator {
    private final Set<String> unauthenticatedURLs; // 로그인이 필요 없는 경로 모음

    public Authenticator() {
        this.unauthenticatedURLs = initUnauthenticatedUrls();
    }

    private Set<String> initUnauthenticatedUrls() {
        Set<String> unauthenticatedUrls = new HashSet<>();
        unauthenticatedUrls.add(HOME_URL);
        unauthenticatedUrls.add(LOGIN_URL);
        unauthenticatedUrls.add(REGISTRATION_URL);
        unauthenticatedUrls.add(IMG_FILE_URL);
        unauthenticatedUrls.add(CSS_FILE_URL);
        return unauthenticatedUrls;
    }

    public boolean isAuthenticated(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getPath();

        if (isLoginCheckPath(requestPath)) { // 로그인이 필요한 경로인지 검사
            boolean isLoggedIn = isLoggedIn(request);
            if (!isLoggedIn) { // 로그인이 안되어 있으면 로그인 화면으로 리다이렉트
                setRedirectURL(response, requestPath);
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

    private boolean isLoginCheckPath(String requestPath) {
        // 로그인이 필요한 경로인지 확인
        return unauthenticatedURLs.stream()
                .noneMatch(mappedURL -> isMatched(mappedURL, requestPath));
    }

    private boolean isMatched(String mappedURL, String requestPath) {
        return requestPath.matches(mappedURL);
    }

    private void setRedirectURL(HttpResponse response, String requestPath) throws IOException {
        // 사용자가 요청한 경로를 쿼리 파라미터로 담고 로그인 화면으로 리다이렉트
        response.sendRedirect(LOGIN_URL + "?redirectURL=" + requestPath);
    }
}
