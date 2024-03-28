package webserver;

import Utils.RouteManager;
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

    private void processRequest() {
        try {
            HttpRequestReader requestReader = new HttpRequestReader(connection);
            String httpRequest = requestReader.readRequest();
            HttpRequest request = new HttpRequest(httpRequest);
            HttpResponseWriter responseWriter = new HttpResponseWriter(connection);

            // 예시: 로그인 경로에 대한 요청 처리
            if ("/create".equals(request.getPath())) {
                responseWriter.sendRedirect("/index.html");
                return; // 리디렉션 후에는 더 이상의 처리를 중단
            }

            String firstPath = RouteManager.getFilePath(request.getPath());
            new HttpResponse(responseWriter, firstPath);
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