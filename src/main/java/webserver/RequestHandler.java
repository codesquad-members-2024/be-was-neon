package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            HttpRequestMsg httpRequestMsg = new HttpRequestMsg(in);

            // HTTP 헤더에서 requestTarget 추출, 알맞는 핸들러 호출 후 결과 반환
            Router router = new Router();
            HttpResponseMsg httpResponseMsg = router.route(httpRequestMsg);

            // 처리 결과를 바탕으로 HTTP 응답 메시지를 만들어 클라이언트에 전송
            DataOutputStream dos = new DataOutputStream(out);
            httpResponseMsg.send(dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
