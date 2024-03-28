package webserver.handler;

import java.io.*;
import java.net.Socket;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.reader.HttpRequestReader;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.router.HttpRequestHandlerRouter;

public class ConnectionHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    private Socket connection;

    public ConnectionHandler(Socket connection) {
        this.connection = connection;
    }


    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());


        // Request Handler 생성
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            // InputStream을 읽어 HttpRequest 객체를 만들어내는 클래스
            HttpRequestReader requestReader = new HttpRequestReader(in);

            // HttpRequest 객체 생성
            HttpRequest httpRequest = requestReader.getRequest();   // 읽은 후, HttpRequest 객체를 생성함

            // HttpRequestHandler를 생성하기 위한 Router 생성
            HttpRequestHandlerRouter handlerRouter = new HttpRequestHandlerRouter();

            // router를 통해 HttpRequestHandler 생성
            HttpRequestHandler httpRequestHandler = handlerRouter.getRequestHandler(httpRequest);

            // httpReuqestHandler가 동작 -> HttpResponse 객체를 생성한다.
            HttpResponse httpResponse = httpRequestHandler.handleRequest(httpRequest);

            HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

            httpResponseHandler.handleResponse(httpResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            return;
        }
    }
}
