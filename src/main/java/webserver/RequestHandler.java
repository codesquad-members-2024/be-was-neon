package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.HashMap;
import java.util.Map;
import model.UserRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    //ENUM 으로 변경하자
    private static final String DEFAULT_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";
    private static final String REGISTRATION = "/registration";
    private static final String CREATE = "/create";

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {// Runnable : run 메서드를 구현
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
            connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            //startLine에서 request타겟을 다 분리해서 path와 query스트링으로 나눠서 가져오는게 좋겠는데?
            String[] pathAndQuery = HttpRequest.getRequestTarget(in, logger);

            String path = pathAndQuery[0];
            Map<String, String> queryString = new HashMap<>();

            if (pathAndQuery.length == 2) {
                queryString = HttpRequest.getQueryString(pathAndQuery[1]);
            }

            //여기에 경로를 설정해주는 코드 작성
            // registration이 들어오면?
            String filePath = DEFAULT_PATH + path;

            //controller 역할
            if (path.equals(INDEX_HTML)) {
                filePath = DEFAULT_PATH + INDEX_HTML;

                HttpResponse.sendHttpResponse(out, filePath);
            }
            if (path.equals(REGISTRATION)) {
                filePath = DEFAULT_PATH + REGISTRATION + INDEX_HTML;

                HttpResponse.sendHttpResponse(out, filePath);
            }
            if (path.equals(CREATE)) {
                boolean isSuccess = UserRegistration.registration(queryString);
                //redirect구현
                if (isSuccess) {
                    HttpResponse.redirectHttpResponse(out, INDEX_HTML);
                }
                if (!isSuccess) { // 가입 실패하면 그 페이지 그대로
                    HttpResponse.redirectHttpResponse(out, REGISTRATION);
                }
            }
            HttpResponse.sendHttpResponse(out, filePath);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
