package webserver;

import static utils.Constant.CHARSETS;
import static utils.Constant.CRLF;
import static utils.Constant.LINE_FEED;

import http.HttpRequestRouter;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.Status;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String NEW_CLIENT_CONNECT_MESSAGE = "New Client Connect! Connected IP : {}, Port : {}";

    private Socket connection;
    private DataOutputStream dos;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;


    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.httpRequest = new HttpRequest();
        this.httpResponse = new HttpResponse();
    }

    public void run() {
        logger.debug(NEW_CLIENT_CONNECT_MESSAGE, connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(in, CHARSETS);
            BufferedReader br = new BufferedReader(inputStreamReader);
            dos = new DataOutputStream(out);

            initializeHttpRequest(br);
            HttpRequestRouter httpRequestRouter = new HttpRequestRouter(httpRequest, httpResponse);
            httpRequestRouter.processRequest();
            sendResponse(httpResponse);
        } catch (IOException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }

    private void initializeHttpRequest(BufferedReader br) throws IOException {
        String request = br.readLine();

        setRequestFirstLine(request);
        setRequestHeadersAndBody(br, request);

        logger.info(httpRequest.getFormattedRequest());
    }

    private void setRequestFirstLine(String request) throws IOException {
        try {
            httpRequest.setFirstLine(request);
        } catch (FileNotFoundException e) {
            httpResponse.setErrorResponse(Status.NOT_FOUND);
            sendResponse(httpResponse);
            throw new FileNotFoundException();
        }
    }

    private void setRequestHeadersAndBody(BufferedReader br, String request) throws IOException {
        StringBuilder headers = new StringBuilder();
        StringBuilder body = new StringBuilder();
        while (!(request = br.readLine()).equals("")) {
            if (request.equals(" ")) { // requestBody가 있는 경우
                while (!(request = br.readLine()).equals("")) {
                    body.append(request).append(LINE_FEED);
                }
                break;
            }
            headers.append(request).append(LINE_FEED);
        }
        httpRequest.setRequestHeaders(headers.toString());
        httpRequest.setBody(body.toString());
    }

    private void writeHeader(HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getFormattedFirstLine());
            dos.writeBytes(CRLF);
            dos.writeBytes(httpResponse.getFormattedContentType() + CRLF);
            dos.writeBytes(httpResponse.getFormattedContentLength() + CRLF);
            dos.writeBytes(CRLF);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeBody(HttpResponse httpResponse) {
        try {
            byte[] body = httpResponse.getHttpRequestBody();
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(HttpResponse httpResponse) throws IOException {
        writeHeader(httpResponse);
        writeBody(httpResponse);
        dos.flush();
    }
}
