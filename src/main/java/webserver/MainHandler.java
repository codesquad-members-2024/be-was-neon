package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httprequest.HttpRequest;
import webserver.httprequest.HttpRequestHandler;
import webserver.httprequest.HttpRequestProcessor;
import webserver.httpresponse.HttpResponse;
import webserver.httpresponse.HttpResponseProcessor;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    private final Socket connection;

    public MainHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest httpRequest = new HttpRequest(new HttpRequestProcessor(bufferedReader));
            httpRequest.printHeaderLinesLog();

            HttpResponse httpResponse = HttpRequestHandler.getUriProcessResult(httpRequest);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponseProcessor httpResponseProcessor = new HttpResponseProcessor(dos, httpResponse);
            httpResponseProcessor.sendResponse();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
