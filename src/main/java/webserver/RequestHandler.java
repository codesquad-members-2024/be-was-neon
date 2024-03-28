package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.RequestManager;
import webserver.requesthandler.Handler;
import webserver.response.HttpResponse;
import webserver.response.ResponseManager;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            RequestManager requestManager = new RequestManager();
            HandlerMapping handlerMapping = new HandlerMapping();
            ResponseManager responseManager = new ResponseManager();

            HttpRequest httpRequest = requestManager.parseInputStream(in);

            Handler handler = handlerMapping.findHandler(httpRequest);
            HttpResponse httpResponse = responseManager.processResponse(httpRequest, handler);
            httpResponse.send(out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
