package webserver.handler.requesthandler;

import config.Config;
import db.Database;
import db.SessionStore;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.sid.SidGenerator;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.QueryUtils;

import java.util.Map;

public class LoginHandler implements HttpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    private static final String LOGIN_SUCCESS_URL = "/";
    private static final String LOGIN_FAIL_URL = "/user/login_failed/index.html";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        switch (httpRequest.getMethod()) {
            case GET -> {
                return handleGet(httpRequest);
            }
            case POST -> {
                return handlePost(httpRequest);
            }
            default -> {
                return HttpResponseUtils.get404Response();  // GET, POST 요쳥이 아닌 경우
            }
        }
    }

    // GET 요청
    private HttpResponse handleGet(HttpRequest httpRequest) {
        httpRequest = HttpRequestUtils.convertToStaticFileRequest(httpRequest);
        StaticFileHandler staticFileHandler = new StaticFileHandler();
        return staticFileHandler.handleRequest(httpRequest);
    }


    // POST 요청
    private HttpResponse handlePost(HttpRequest httpRequest) {
        String header;
        try {
            User user = loginUser(httpRequest);
            String sid = createSid(user);
            header = HttpResponseUtils.generateRedirectResponseHeader(LOGIN_SUCCESS_URL);
            header = HttpResponseUtils.setCookieHeader(header, sid);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            header = HttpResponseUtils.generateRedirectResponseHeader(LOGIN_FAIL_URL);
        }
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        return new HttpResponse(startLine, header);
    }

    private User loginUser(HttpRequest httpRequest) {
        Map<String, String> queries = QueryUtils.getQueries(httpRequest.getBody());

        String inputId = queries.get("userId");
        String inputPassword = queries.get("password");

        User user = Database.findUserById(inputId);

        validateUser(user, inputId, inputPassword); // 유저 정보 확인

        return user;
    }

    private void validateUser(User user, String inputId, String inputPassword) {
        if (user == null) {
            throw new IllegalArgumentException("[ERROR] 등록되지 않은 사용자입니다.");
        }
        if (!(user.getPassword().equals(inputPassword) && user.getUserId().equals(inputId))) {
            throw new IllegalArgumentException("[ERROR] 비밀번호가 일치하지 않습니다.");
        }
    }

    private String createSid(User user) {
        String sid = SidGenerator.getRandomSid();
        SessionStore.addSession(sid, user);
        return sid;
    }


}
