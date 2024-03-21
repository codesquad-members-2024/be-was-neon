package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.RequestManager;
import response.ResponseManager;

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
            RequestManager requestManager = new RequestManager(in);
            ResponseManager responseManager = new ResponseManager(requestManager);

            requestManager.readRequestMessage();

            DataOutputStream dos = new DataOutputStream(out);
            responseManager.setResponse();
            responseManager.writeResponse(dos);
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}