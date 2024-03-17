package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final String RELATIVE_PATH = "./src/main/resources/static";
    private static final String TEXT_HTML = "text/html";
    private static final String TEXT_CSS = "text/css";
    private static final String IMAGE_SVG_XML = "image/svg+xml";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.parseRequestLines(in);

            DataOutputStream dos = new DataOutputStream(out);
            // httpRequest 의 타입에 따라 다른 response 를 보내줄수있게 해주었습니다.
            RequestHandler.handleRequest(httpRequest.getRequestType(),dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    public static void handleRequest(String requestURL, DataOutputStream dos) throws IOException {
        if (requestURL.endsWith(".html")) {
            HttpResponse.respondHtmlFile(dos,RELATIVE_PATH + requestURL, TEXT_HTML);
        } else if (requestURL.endsWith(".css")) {
            HttpResponse.respondHtmlFile(dos,RELATIVE_PATH + requestURL, TEXT_CSS);
        } else if (requestURL.endsWith(".svg")) {
            HttpResponse.respondHtmlFile(dos,RELATIVE_PATH + requestURL, IMAGE_SVG_XML);
        }

        if (requestURL.startsWith("/create")) {
            RegistrationResponse.respondRegistration(dos, requestURL);
        }
    }
}
