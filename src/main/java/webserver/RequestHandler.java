package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

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
            //http Request 정보 설정
            HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(in);
            httpRequestBuilder.readHttpRequest();
            HttpRequest httpRequest = new HttpRequest(httpRequestBuilder);
            //HTTP Response 정보 설정
            ResponseHandler responseHandler = new ResponseHandler();
            //requestUri 를 통해서 해당하는 동작 실행 + HttpMethod 를 통해 실행할 동작을 찾는다.
            String filePath = responseHandler.select(httpRequest);
            //response를 보낸다
            //여기서 보내는 방법을 결정해야하는데 리다이렉트 여부 결정해서 보내준다.
            if(filePath.startsWith("redirect:")){
                filePath = filePath.substring(filePath.indexOf(":")+1);
                HttpResponse.redirectHttpResponse(out, filePath);
            }else{
                HttpResponse.sendHttpResponse(out,filePath);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
