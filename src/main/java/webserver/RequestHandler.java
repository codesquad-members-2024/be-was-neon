package webserver;

import db.Database;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import java.util.HashMap;
import java.util.StringTokenizer;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    private static final String DEFAULT_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";
    private static final String REGISTRATION = "/registration";
    private static final String QUESTION_MARK = "?";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {// Runnable : run 메서드를 구현
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            //startLine에서 요청 대상만 가져온다
            String requestTarget = HttpRequest.getRequestTarget(in, logger);

            // registration이 들어오면?
            String filePath = DEFAULT_PATH + requestTarget;
            //여기에 경로를 설정해주는 코드 작성
            if (!requestTarget.contains(QUESTION_MARK)) {
                if (requestTarget.equals(INDEX_HTML)) {
                    filePath = DEFAULT_PATH + INDEX_HTML;
                }
                if (requestTarget.equals(REGISTRATION)){
                    filePath = DEFAULT_PATH + REGISTRATION + INDEX_HTML;
                }

                //HttpResponse를 생성해서 보낸다.
                HttpResponse.sendHttpResponse(out,filePath);
            }

            if (requestTarget.contains(QUESTION_MARK)) {
                int queryStringStartIndex = requestTarget.indexOf(QUESTION_MARK);

                //HttpResponse를 생성해서 보낸다.
                String queryString = requestTarget.substring(queryStringStartIndex);
                StringTokenizer tokenizer = new StringTokenizer(queryString,"&");
//                HashMap<String,String> hashMap = new HashMap();

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    System.out.println(token);
                }

                Database.addUser(new User("1","2","3","tmdgus717@naver.com"));
                HttpResponse.sendHttpResponse(out,DEFAULT_PATH + INDEX_HTML);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
