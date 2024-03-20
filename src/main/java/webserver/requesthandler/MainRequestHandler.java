package webserver.requesthandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.requesthandler.authenticator.Authenticator;
import webserver.requesthandler.authenticator.UnauthenticatedURLs;
import webserver.requesthandler.handlerMapper.RequestHandlerMapper;
import webserver.requesthandler.handlerimpl.RequestHandler;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;

public class MainRequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    private final Socket connection;
    private final RequestHandlerMapper requestHandlerMapper;

    public MainRequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        requestHandlerMapper = new RequestHandlerMapper();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            Authenticator authenticator = new Authenticator(new UnauthenticatedURLs());
            boolean isAuthenticated = authenticator.isAuthenticated(request, response);// 인증이 필요한 페이지에 접근하는지 확인

            if (!isAuthenticated) {
                return;
            }
            RequestHandler requestHandler = requestHandlerMapper.findRequestHandler(request);
            handleMethod(request, response, requestHandler);
            response.send();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void handleMethod(HttpRequest request, HttpResponse response, RequestHandler requestHandler)
            throws IOException {
        if (request.isGET()) {
            requestHandler.handleGet(request, response);
        }
        if (request.isPOST()) {
            requestHandler.handlePost(request, response);
        }
    }
}
