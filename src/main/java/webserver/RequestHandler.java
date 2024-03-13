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
            String requestTarget = httpRequest.getRequestTarget();

            HttpResponse httpResponse = requestMapper.getHttpResponse(requestTarget);
            send(out, httpResponse);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void send(OutputStream out, HttpResponse httpResponse) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            writeHeader(httpResponse.getHeader(), dos);
            writeBody(httpResponse.getBody(), dos);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeBody(byte[] body, DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }

    private static void writeHeader(List<String> httpResponse, DataOutputStream dos) throws IOException {
        for (String line : httpResponse) {
            dos.writeBytes(line);
        }
    }

}
