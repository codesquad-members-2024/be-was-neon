package webserver.requesthandler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.*;
import webserver.request.HttpRequest;
import webserver.response.CreateHeader;
import webserver.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class RegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

    @Override
    public HttpResponse getRequest(HttpRequest httpRequest) {
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
        addUser(httpRequest);
        CreateHeader createHeader = new CreateHeader();

        return new HttpResponse(createHeader.redirect302(UrlConvertor.INDEX_URL));
    }

    private void addUser(HttpRequest httpRequest) throws IOException {
        Map<String, String> parsedData = httpRequest.parseData();
        User user = new User(parsedData.get("userId"), parsedData.get("password"), parsedData.get("name"), parsedData.get("email"));

        Database.addUser(user);
        logger.debug("유저 등록 완료!! : {}", Database.findAll());
    }
}
