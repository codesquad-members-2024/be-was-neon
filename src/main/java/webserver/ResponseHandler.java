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
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DOT = ".";
    public String select(HttpRequest httpRequest, OutputStream out)
        throws UnsupportedEncodingException {

        String requestPath = httpRequest.getPath();
        String httpMethod = httpRequest.getHttpMethod();

        //controller 역할
        if(httpMethod.equals(GET) && requestPath.contains(INDEX_HTML)){
            return DEFAULT_PATH + requestPath;
        }

        if (httpMethod.equals(GET) && requestPath.contains(REGISTRATION)) {
            // src/main/resources/static + /registration/index.html
            return DEFAULT_PATH + requestPath + INDEX_HTML;
        }

        if (httpMethod.equals(GET)) {
            if(!requestPath.contains(DOT)){
                return DEFAULT_PATH + INDEX_HTML;
            }
        }

        if(httpMethod.equals(POST) && requestPath.contains(CREATE)){
            //POST 메소드로 변경하면서 에러 발생
            //UserRegistration.register(httpRequest.getQueryString());
            //redirect로 보내야 한단 말이지
            UserRegistration.register(httpRequest.getHttpBody().getKeyValue());
            return DEFAULT_PATH + INDEX_HTML;
        }

        return DEFAULT_PATH + requestPath;
    }
}
