package webserver.requesthandler.url;

import static webserver.requesthandler.url.URLConst.CSS_FILE_URL;
import static webserver.requesthandler.url.URLConst.HOME_URL;
import static webserver.requesthandler.url.URLConst.IMG_FILE_URL;
import static webserver.requesthandler.url.URLConst.LOGIN_URL;
import static webserver.requesthandler.url.URLConst.REGISTRATION_URL;

import java.util.HashSet;
import java.util.Set;

public class UnauthenticatedURLs {
    private final Set<String> store;

    public UnauthenticatedURLs() {
        store = new HashSet<>();
        store.add(HOME_URL);
        store.add(LOGIN_URL);
        store.add(REGISTRATION_URL);
        store.add(IMG_FILE_URL);
        store.add(CSS_FILE_URL);
    }

    public boolean isLoginCheckPath(String requestPath) {
        // 로그인이 필요한 경로인지 확인
        return store.stream()
                .noneMatch(mappedURL -> isMatched(mappedURL, requestPath));
    }

    private boolean isMatched(String mappedURL, String requestPath) {
        return requestPath.matches(mappedURL);
    }
}
