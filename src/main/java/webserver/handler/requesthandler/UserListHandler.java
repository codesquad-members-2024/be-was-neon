package webserver.handler.requesthandler;

import config.Config;
import db.Database;
import db.SessionStore;
import model.User;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.SidUtils;

import java.io.*;
import java.util.StringJoiner;

public class UserListHandler implements HttpRequestHandler {
    private static final String REDIRECT_URL = "/login";
    private static final String COOKIE = "Cookie";

    private static final String PATTERN = "<ul(?:\\s+class=\"ul-user\"*\")?>[\\s\\S]*?</ul>";

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
            String replacement = getAllUser();
            return dynamicFileHandler.handleRequest(replacement, PATTERN);
        }
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = HttpResponseUtils.generateRedirectResponseHeader(REDIRECT_URL);
        return new HttpResponse(startLine, header);
    }

    private boolean isLoggedIn(HttpRequest httpRequest) {
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

    private String getAllUser() {
        StringJoiner joiner = new StringJoiner("\n");
        for (User user : Database.findAll()) {
            joiner.add("<li>" + user.getName() + "</li>");
        }
        return joiner.toString();
    }

}
