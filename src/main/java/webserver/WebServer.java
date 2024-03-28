package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    // 서버 기본 포트와 쓰레드 풀 크기 설정
    private static final int DEFAULT_PORT = 8080;
    private static final int THREAD_POOL_SIZE = 11;

    public static void main(String[] args) throws Exception {
        int port = determinePort(args);

        // 고정 크기의 쓰레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started on port {}.", port);

            while (true) {
                try {
                    Socket connection = listenSocket.accept();
                    Runnable requestHandler = new RequestHandler(connection);
                    executorService.execute(requestHandler); // 연결 처리를 위한 쓰레드 할당
                } catch (Exception e) {
                    logger.error("Connection error", e);
                }
            }
        } finally {
            if (!executorService.isShutdown()) {
                executorService.shutdown(); // 서버 종료 시 쓰레드 풀 종료
            }
        }
    }

    // 포트 번호 결정 로직
    private static int determinePort(String[] args) {
        if (args == null || args.length == 0) {
            return DEFAULT_PORT;
        } else {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.error("Port must be a number, defaulting to {}", DEFAULT_PORT);
                return DEFAULT_PORT;
            }
        }
    }
}
