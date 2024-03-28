package webserver;

import Redirect.RedirectSelector;
import Redirect.RequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } finally {
            closeConnection();
        }
    }

    // HttpRequestReader를 사용하여 요청을 읽고, 해당 요청을 처리하기 위해 HttpRequest와 HttpResponseWriter를 활용
    private void processRequest() {
        try {
            HttpRequestReader requestReader = new HttpRequestReader(connection);
            String httpRequest = requestReader.readRequest();
            HttpRequest request = new HttpRequest(httpRequest);
            HttpResponseWriter responseWriter = new HttpResponseWriter(connection); // 응답 작성을 위한 객체

            // RedirectManager를 통해 요청 경로에 적합한 RequestProcessor를 찾아 요청 처리
            RequestProcessor processor = RedirectSelector.getProcessor(request.getPath());
            if (processor != null) {
                processor.handleFilerequest(request, responseWriter);
            } else {
                responseWriter.sendError404();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            logger.error("Error closing socket: " + e.getMessage());
        }
    }
}
