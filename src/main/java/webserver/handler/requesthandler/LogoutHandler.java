package webserver.handler.requesthandler;

import config.Config;
import db.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpResponseUtils;
import webserver.utils.SidUtils;

public class LogoutHandler implements HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    private static final String ROOT_URL = "/";
    private static final String COOKIE = "Cookie";

    private static final String SPACE = " ";

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
        String header = HttpResponseUtils.generateRedirectResponseHeader(ROOT_URL);
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        try {
            String sid = getSid(httpRequest);
            String expireSid = getExpireSid(sid);
            SessionStore.expireSid(sid);
            header = HttpResponseUtils.setCookieHeader(header, expireSid);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
        return new HttpResponse(startLine, header);
    }

    private String getSid(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaderValue(COOKIE);
        if (cookie == null) {
            throw new IllegalArgumentException("[ERROR] 쿠키가 없습니다.");
        }
        return SidUtils.getCookieSid(cookie);
    }

    private String getExpireSid(String sid) {
        return sid + ";" + SPACE + "max-age=0";
    }
}
