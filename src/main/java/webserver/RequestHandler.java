package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = Request.makeRequest(in);
            log.info(request.getLog());

            DataOutputStream dos = new DataOutputStream(out);

            Response.sendResponse(dos , request);
        } catch (IOException | ArrayIndexOutOfBoundsException e) { // 리퀘스트 읽을 때 발생 예외
            log.error(e.getMessage());
        }
    }
}