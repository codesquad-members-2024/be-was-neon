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
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
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
            sendResponse();
        } catch (IOException | IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }

    private void initializeHttpRequest(BufferedReader br) throws IOException {
        setRequestFirstLine(br);
        setRequestHeaders(br);
        setRequestBody(br);

        logger.info(httpRequest.getFormattedRequest());
    }

    private void setRequestFirstLine(BufferedReader br) throws IOException {
        String request = getFirstLine(br);
        try {
            httpRequest.setFirstLine(request);
        } catch (FileNotFoundException e) {
            httpResponse.setErrorResponse(Status.NOT_FOUND);
            sendResponse();
            throw new FileNotFoundException();
        }
    }

    private String getFirstLine(BufferedReader br) throws IOException {
        String request = br.readLine();
        if (request == null) {
            throw new IOException();
        }
        return request;
    }

    private void setRequestHeaders(BufferedReader br) throws IOException {
        StringBuilder headers = new StringBuilder();
        String request;
        while (!(request = br.readLine()).equals("")) {
            headers.append(request).append(LINE_FEED);
        }
        httpRequest.setRequestHeaders(headers.toString());
    }

    private void setRequestBody(BufferedReader br) throws IOException {
        int contentLength;
        Optional<String> contentLengthHeader = httpRequest.getContentLength();
        if (contentLengthHeader.isEmpty()) {
            return;
        }
        contentLength = Integer.parseInt(contentLengthHeader.get().trim());
        String body = generateRequestBody(br, contentLength);
        httpRequest.setBody(body);
    }

    private String generateRequestBody(BufferedReader br, int contentLength) throws IOException {
        StringBuilder body = new StringBuilder();
        char[] buffer = new char[1024];
        int bytesRead;
        int totalBytesRead = 0;
        while (totalBytesRead < contentLength
                && (bytesRead = br.read(buffer, 0, Math.min(buffer.length, contentLength - totalBytesRead))) != -1) {
            body.append(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
        }
        return body.toString();
    }

    private void sendResponse() throws IOException {
        try {
            byte[] body = httpResponse.getHttpRequestBody();
            dos.writeBytes(httpResponse.getFormattedResponse());
            dos.writeBytes(CRLF);
            dos.write(body, 0, body.length);
            logger.info(httpResponse.getFormattedResponse());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        dos.flush();
    }
}
