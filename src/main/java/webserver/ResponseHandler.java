package webserver;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import model.HttpRequest;
import model.UserRegistration;

public class ResponseHandler {
    private static final String DEFAULT_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";
    private static final String REGISTRATION = "/registration";
    private static final String CREATE = "/create";
    public void select(HttpRequest httpRequest, OutputStream out)
        throws UnsupportedEncodingException {

        String filePath = DEFAULT_PATH + httpRequest.getPath();

        //controller 역할
        if (httpRequest.getPath().equals(INDEX_HTML)) {
            filePath = DEFAULT_PATH + INDEX_HTML;

            HttpResponse.sendHttpResponse(out, filePath);
        }
        if (httpRequest.getPath().equals(REGISTRATION)) {
            filePath = DEFAULT_PATH + REGISTRATION + INDEX_HTML;

            HttpResponse.sendHttpResponse(out, filePath);
        }
        if (httpRequest.getPath().equals(CREATE)) {
            boolean isSuccess = UserRegistration.register(httpRequest.getQueryString());
            //redirect구현
            if (isSuccess) {
                HttpResponse.redirectHttpResponse(out, INDEX_HTML);
            }
            if (!isSuccess) { // 가입 실패하면 그 페이지 그대로
                HttpResponse.redirectHttpResponse(out, REGISTRATION);
            }
        }
        HttpResponse.sendHttpResponse(out, filePath);
    }
}
