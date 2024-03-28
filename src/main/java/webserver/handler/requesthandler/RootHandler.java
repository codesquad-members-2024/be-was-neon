package webserver.handler.requesthandler;

import config.Config;
import db.SessionStore;
import model.User;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.SidUtils;

public class RootHandler implements HttpRequestHandler {
    private static final String COOKIE = "Cookie";
    private static final String DEFAULT_FILE = "index.html";
    private static final String PATTERN = "<ul(?:\\s+class=\"[^\"]*\")?>[\\s\\S]*?</ul>";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case GET -> {
                return handleGet(httpRequest);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET 요쳥이 아닌 경우
            }
        }
    }

    private HttpResponse handleGet(HttpRequest httpRequest) {
        httpRequest = HttpRequestUtils.convertToStaticFileRequest(httpRequest);
        if (isLoggedIn(httpRequest)) {
            DynamicFileHandler dynamicFileHandler = new DynamicFileHandler(httpRequest);
            String userName = getUserName(httpRequest);
            String replacement = getReplacement(userName);
            return dynamicFileHandler.handleRequest(replacement, PATTERN);
        }
        StaticFileHandler staticFileHandler = new StaticFileHandler();
        return staticFileHandler.handleRequest(httpRequest);
    }

    private boolean isLoggedIn(HttpRequest httpRequest) {
        // index.html 파일에 대해서만 응답!
        if (!isDefaultFile(httpRequest)) {
            return false;
        }

        String cookie = httpRequest.getHeaderValue(COOKIE);

        try {
            String sid = SidUtils.getCookieSid(cookie);
            return isValidSid(sid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidSid(String sid) {
        return SessionStore.sessionIdExists(sid);
    }

    private boolean isDefaultFile(HttpRequest httpRequest) {
        return httpRequest.getRequestLine().endsWith(DEFAULT_FILE);
    }

    private String getUserName(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaderValue(COOKIE);
        String sid = SidUtils.getCookieSid(cookie);
        User user = SessionStore.getUserBySid(sid);
        return user.getName();
    }

    private String getReplacement(String userName) {

        return "<ul class=\"header__menu\">\n" +
                "<li class=\"user-name\">" + userName + "</li>\n" +
                "    <li class=\"header__menu__item\">\n" +
                "        <a class=\"btn btn_contained btn_size_s\" href=\"/article\">글쓰기</a>\n" +
                "    </li>\n" +
                "    <li class=\"header__menu__item\">\n" +
                "        <button id=\"logout-btn\" class=\"btn btn_ghost btn_size_s\" onclick=\"window.location.href='/logout'\">로그아웃</button>\n" +
                "    </li>\n" +
                "</ul>";
    }
}
