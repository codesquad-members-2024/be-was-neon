package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HandlerMapping;
import webserver.RequestHandler;
import webserver.request.HttpRequest;
import webserver.requesthandler.Handler;

import java.io.IOException;

public class ResponseManager {

    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    private static final Logger logger = LoggerFactory.getLogger(ResponseManager.class);

    public HttpResponse processResponse(HttpRequest httpRequest, Handler handler) throws IOException {
        if (httpRequest.getHttpMethod().equals(GET_METHOD)) {
            return handler.getRequest(httpRequest);
        } else if (httpRequest.getHttpMethod().equals(POST_METHOD)) {
            return handler.postRequest(httpRequest);
        }

        //올바른 메서드가 아닌 경우 서버 오류 응답 반환
        logger.error("Unexpected error in processing HTTP request");
        return createServerErrorResponse();
    }

    private HttpResponse createServerErrorResponse() {
        CreateHeader createHeader = new CreateHeader();
        return new HttpResponse(createHeader.serverError500());
    }
}
