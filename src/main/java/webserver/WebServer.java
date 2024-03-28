package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.handler.ConnectionHandler;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int CORE_THREAD_SIZE = 3;
    private static final int MAX_THREAD_SIZE = 100;
    private static final long REST_TIME = 120;


    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = Config.getPort();
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            ExecutorService executor = new ThreadPoolExecutor(
                    CORE_THREAD_SIZE,          // 코어 스레드 개수
                    MAX_THREAD_SIZE,        // 최대 스레드 개수
                    REST_TIME,       // 놀고 있는 시간
                    TimeUnit.SECONDS,   // 놀고 있는 시간 단위
                    new SynchronousQueue<Runnable>()    // 작업 큐
            );

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executor.execute(new ConnectionHandler(connection));
            }

            // 서버가 종료되면 executor을 shutdown
            executor.shutdown();
        }

    }
}
