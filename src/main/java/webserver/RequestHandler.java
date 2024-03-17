package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String requestLine = br.readLine();
            // httpRequest 와 header 들을 읽어와 logger 로 console 에 출력해주었습니다.
            String requestURL = HttpRequest.getURL(requestLine);
            //HttpRequest.getRequestHeader(br);

            DataOutputStream dos = new DataOutputStream(out);
            // httpRequest 의 타입에 따라 다른 response 를 보내줄수있게 해주었습니다.
            RequestHandler.handleRequest(requestURL,dos);

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
