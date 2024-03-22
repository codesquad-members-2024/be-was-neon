package webserver;

import Utils.RouteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        try {
            HttpRequest request = new HttpRequest(connection);
            String firstPath = RouteManager.makePath(request.getPath());
            new HttpResponse(connection.getOutputStream(), firstPath);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            // 모든 처리가 끝난 후에 Socket을 안전하게 닫습니다.
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (IOException e) {
                logger.error("Error closing socket: " + e.getMessage());
            }
        }
    }

}
