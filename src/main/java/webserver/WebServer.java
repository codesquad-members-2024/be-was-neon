package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws Exception {
        int port = determinePort(args);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);


        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started on port {}.", port);

            while (true) {
                try {
                    Socket connection = listenSocket.accept();
                    Runnable requestHandler = new RequestHandler(connection);
                    executorService.execute(requestHandler);
                } catch (Exception e) {
                    logger.error("Connection error", e);
                }
            }
        } finally {
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }

    private static int determinePort(String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        } else {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.error("Port must be a number, defaulting to {}", DEFAULT_PORT);
                return DEFAULT_PORT;

            // 서버소켓이 클라이언트의 연결을 수락할 때마다 Runnable 작업을 스레드 풀에 전달
            while(true){
                try{
                    Socket connection = listenSocket.accept();
                    Runnable requestHandler = new RequestHandler(connection);
                    executorService.execute(requestHandler);
                } catch (Exception e){
                    logger.error("연결 오류");
                }
            }
        } finally {
            if(!executorService.isShutdown()){
                executorService.shutdown();
            }
        }
    }
}