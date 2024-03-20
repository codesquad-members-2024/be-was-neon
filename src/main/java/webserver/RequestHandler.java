package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.Handler;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpRequestReader;
import webserver.httpMessage.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequestReader reader = new HttpRequestReader(in);
            HttpRequest httpRequest = reader.readHttpRequest();

            Handler handler = RequestMapper.findHandler(httpRequest);

            HttpResponse httpResponse = handler.service(httpRequest);
            httpResponse.send(out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
