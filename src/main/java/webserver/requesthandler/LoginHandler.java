package webserver.requesthandler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.FileReader;
import webserver.SessionManager;
import webserver.UrlConvertor;
import webserver.request.HttpRequest;
import webserver.response.CreateHeader;
import webserver.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class LoginHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public HttpResponse getRequest(HttpRequest httpRequest) throws IOException {
        String requestTarget = httpRequest.getRequestTarget();
        CreateHeader createHeader = new CreateHeader();
        UrlConvertor urlConvertor = new UrlConvertor();

        String filePath = urlConvertor.urlController(requestTarget);
        String type = urlConvertor.typeController(requestTarget);

        File file = new File(filePath);
        // 파일이 존재하는지
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader();
                byte[] body = fileReader.responseBody(file);
                return new HttpResponse(createHeader.generate200((int) file.length(), type), body);
            } catch (Exception e) {
                logger.error(e.getMessage());
                // 파일을 읽다가 오류 발생하면 500 응답 반환
                return new HttpResponse(createHeader.serverError500());
            }
        } else {
            // 요청된 파일이 존재하지 않는 경우 404 응답 반환
            return new HttpResponse(createHeader.notFound404());
        }
    }

    @Override
    public HttpResponse postRequest(HttpRequest httpRequest) throws IOException {
        Map<String, String> dataMap = httpRequest.parseData();
        String userId = dataMap.get("userId");
        String password = dataMap.get("password");

        User user = loginSuccess(userId, password);

        if (user != null) {
            CreateHeader createHeader = new CreateHeader();
            // sid 생성 후 저장
            String sessionId = SessionManager.createSessionId();
            SessionManager.saveSession(sessionId, user);
            logger.debug("seesionId SAVE: {}", sessionId);

            Optional<User> findUser = SessionManager.findUser(sessionId);
            findUser.ifPresent(data -> logger.debug("seesion SAVE : {}", data));

            return new HttpResponse(createHeader.loginSuccessWithCookie(sessionId));
        } else {
            // 회원 정보가 없을 시
            CreateHeader createHeader = new CreateHeader();
            String redirectLocation = UrlConvertor.LOGIN_FAIL_URL;
            return new HttpResponse(createHeader.redirect302(redirectLocation));
        }
    }


    private User loginSuccess(String userId, String password) {
        return Database.authenticateUser(userId, password);
    }

}
