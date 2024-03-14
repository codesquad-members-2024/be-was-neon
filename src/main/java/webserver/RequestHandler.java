package webserver;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RequestMapper requestMapper;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        requestMapper = new RequestMapper();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = HttpRequest.from(in);
            httpRequest.log();

            HttpResponse httpResponse = requestMapper.service(httpRequest);
            httpResponse.send(out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
