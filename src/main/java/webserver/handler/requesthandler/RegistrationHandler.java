package webserver.handler.requesthandler;

import config.Config;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.StatusCode;
import webserver.handler.HttpRequestHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.utils.HttpRequestUtils;
import webserver.utils.HttpResponseUtils;
import webserver.utils.QueryUtils;

import java.util.Map;


public class RegistrationHandler implements HttpRequestHandler {
    private static final String REDIRECT_URL = "/";
    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

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
        createUser(httpRequest);    // 회원 생성
        String startLine = HttpResponseUtils.generateResponseStartLine(StatusCode.FOUND);
        String header = HttpResponseUtils.generateRedirectResponseHeader(REDIRECT_URL);
        return new HttpResponse(startLine, header);
    }

    private void createUser(HttpRequest httpRequest) {
        Map<String, String> queries = QueryUtils.getQueries(httpRequest.getBody());
        User user = new User(queries.get("userId"), queries.get("password"), queries.get("name"), queries.get("email"));
        Database.addUser(user);
        logger.debug("user created : {}", user);
    }
}
