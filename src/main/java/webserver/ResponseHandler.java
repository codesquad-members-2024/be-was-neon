package webserver;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import model.HttpBody;
import model.HttpRequest;
import model.UserRegistration;

public class ResponseHandler {
    private static final String DEFAULT_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";
    private static final String REGISTRATION = "/registration";
    private static final String CREATE = "/create";
    private static final String REDIRECT = "redirect:";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DOT = ".";
    public String select(HttpRequest httpRequest)
        throws UnsupportedEncodingException {

        String requestPath = httpRequest.getPath();
        String httpMethod = httpRequest.getHttpMethod();

        if(httpMethod.equals(GET) && requestPath.contains(INDEX_HTML)){
            return DEFAULT_PATH + requestPath;
        }

        if (httpMethod.equals(GET) && requestPath.contains(REGISTRATION)) {
            return DEFAULT_PATH + requestPath + INDEX_HTML;
        }

        if (httpMethod.equals(GET)) {
            if(!requestPath.contains(DOT)){
                return DEFAULT_PATH + INDEX_HTML;
            }
        }

        if(httpMethod.equals(POST) && requestPath.contains(CREATE)){
            HttpBody httpBody = httpRequest.getHttpBody();
            UserRegistration.register(httpBody.getKeyValue());
            return REDIRECT+INDEX_HTML;
        }

        return DEFAULT_PATH + requestPath;
    }
}
