package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class URL {
    private static final Logger logger = LoggerFactory.getLogger(URL.class);

//    GET /index.html HTTP/1.1 들어오면 GET과 HTTP/1.1 사이의 값을 추출함
    public static String getTargetURI(BufferedReader br) throws IOException {
        String firstRequest = br.readLine();
        logger.debug("firstRequest : {}", firstRequest);

//        allRequest(br, firstRequest);
        
        String[] split = firstRequest.split(" ");
        String uri = split[1];
        return uri;
    }

//    요청을 모두 받는다.
    private static void allRequest(BufferedReader br, String request) throws IOException{
        while (!request.isEmpty()) {
            logger.debug("request : {}", request);
            request = br.readLine();
        }
    }

    public static File getFile(String uri) {
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        return new File("./src/main/resources/static" + uri);
    }

}
