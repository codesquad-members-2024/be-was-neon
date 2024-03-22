package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import request.RequestParser;
import response.ResponseHandler;

public class MainHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);
    private final Socket connection;

    public MainHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestParser requestParser = new RequestParser();
            HttpRequest httpRequest = requestParser.parse(in);

            logger.debug("Request: {}",httpRequest.getRequestLine());
            logger.debug("Header: {}",httpRequest.getRequestHeader());

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHandler responseHandler = new ResponseHandler(httpRequest);
            responseHandler.sendResponseDependOnRequest(dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
