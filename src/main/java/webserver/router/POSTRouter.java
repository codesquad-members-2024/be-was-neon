package webserver.router;

import webserver.LoginHandler;
import webserver.TargetHandlerExtractor;
import webserver.UserCreateHandler;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.util.NoSuchElementException;

import static webserver.httpMessage.HttpConstants.COOKIE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.*;
import static webserver.httpMessage.HttpStatus.BAD_REQUEST;

public class POSTRouter implements Router {
    private final String redirectLocation = "http://localhost:8080/index.html";
    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest.getRequestTarget());
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler();
                    userCreateHandler.addUserInDatabase(httpRequest.getBody());
                    return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                            .contentType(ContentType.HTML)
                            .addHeaderComponent(LOCATION, redirectLocation)
                            .build();
                }
                case "/login" -> {
                    LoginHandler loginHandler = new LoginHandler();
                    String sessionId = loginHandler.apply(httpRequest.getBody());
                    return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                            .contentType(ContentType.HTML)
                            .addHeaderComponent(COOKIE, String.format("sid=%s; Path=/", sessionId))
                            .addHeaderComponent(LOCATION, redirectLocation)
                            .build();
                }
                default -> {
                    return new HttpResponse.Builder(NOT_FOUND.getStatusCode()
                            , NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다").build();
                }
            }
        } catch (UrlFormatException | IllegalArgumentException e) {
            return new HttpResponse.Builder(BAD_REQUEST.getStatusCode(), BAD_REQUEST.getReasonPhrase() + e.getMessage()).build();
        } catch (NoSuchElementException e) {
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase() + e.getMessage()).build();
        }
    }
}
