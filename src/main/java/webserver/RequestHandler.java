package webserver;

import http.factory.JoinUriFactory;
import http.factory.PageUriFactory;
import http.factory.ResponseFactory;
import http.request.HttpRequest;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HeaderPrinter;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final InputStream in;
    private final OutputStream out;



    public RequestHandler(Socket connection, InputStream in, OutputStream out) {
        this.connection = connection;
        this.in = in;
        this.out = out;
    }

    public void run() {
        debugIp();

        try (
             InputStreamReader inputStreamReader = new InputStreamReader(in);
             BufferedReader br = new BufferedReader(inputStreamReader)) {

            String requestHeader = br.readLine();
            HeaderPrinter.printRequestHeader(br, requestHeader);
            HttpRequest uri = getRequestType(requestHeader);
            respond(uri, requestHeader);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        private HttpRequest getRequestType(String requestHeader) throws IOException {
            return requestHeader.contains("?") ?
                    new JoinUriFactory().getRequest(requestHeader) :
                    new PageUriFactory().getRequest(requestHeader);

        }

        private void respond(HttpRequest requestType, String requestHeader) throws IOException {
        HttpResponse response = ResponseFactory.getResponse(requestType);
        response.respondToRequest(requestType, out, requestHeader);
        }


    private void debugIp() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
    }
}