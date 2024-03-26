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
            HttpRequest request = new HttpRequest(requestReader);

            if(request.getPath().equals("/create")){
                HttpResponseWriter responseWriter = new HttpResponseWriter(connection);
                HttpResponse.sendRedirect(responseWriter, "/index.html");
            }else {
                String firstPath = RouteManager.getFilePath(request.getPath());
                HttpResponseWriter responseWriter = new HttpResponseWriter(connection);
                new HttpResponse(responseWriter, firstPath);
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

