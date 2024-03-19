package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {// Runnable : run 메서드를 구현
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
            connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            //http 정보 설정
            HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(in);
            HttpRequest httpRequest = new HttpRequest(httpRequestBuilder);
            //HTTP Response를 생성하는 responseHandler 생성
            ResponseHandler responseHandler = new ResponseHandler();
            //requestUri 를 통해서 해당하는 동작 실행
            responseHandler.select(httpRequest, out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
