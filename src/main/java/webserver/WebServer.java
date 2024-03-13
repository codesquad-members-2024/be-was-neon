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
    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // html파일을 응답하는 웹 서버의 경우 일반적으로 I/O 바운드 작업이 주이기 때문에 스레드풀을 동적으로 사용할 수 있는 캐시된 스레드 풀 사용
        ExecutorService threadPool = Executors.newCachedThreadPool();

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            while (true) {
                // 클라이언트가 연결될 때마다 스레드 풀에서 작업 할당
                Socket connection = listenSocket.accept();
                threadPool.execute(new RequestHandler(connection));
            }
        } finally {
            //작업 큐의 작업까지 모두 처리 후 종료
            threadPool.shutdown();
        }
    }
}
