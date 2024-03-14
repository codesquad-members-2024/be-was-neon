package util;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class URL {
    private static final Logger logger = LoggerFactory.getLogger(URL.class);
    private static final int USER_ID = 0;
    private static final int USER_NAME = 1;
    private static final int USER_PASSWORD = 2;

//    GET /index.html HTTP/1.1 들어오면 GET과 HTTP/1.1 사이의 값을 추출함
    public static String getTargetURI(BufferedReader br) throws IOException {
        String firstRequest = br.readLine();
        logger.debug("firstRequest : {}", firstRequest);

//        allRequest(br, firstRequest);
        
//        /create, /index 에따라 다르게 보여줘야함
        return checkRequest(firstRequest);
    }

//    요청을 모두 받는다.
    private static void allRequest(BufferedReader br, String request) throws IOException{
        while (!request.isEmpty()) {
            logger.debug("request : {}", request);
            request = br.readLine();
        }
    }

//    /create, /index 등등 여기서 각각에 맞게 처리한다
    private static String checkRequest(String rawRequest) {
        if(!rawRequest.startsWith("GET")) return "/index.html";
        String[] split = rawRequest.split(" ");
        String uri = split[1];

        if (uri.startsWith("/create")) {
            int index = uri.indexOf("?");
            String query = uri.substring(index + 1);

//            query = "userId=blah&name=blah&password=blah
            String[] information = query.split("&");
            for(int i=0; i<information.length; i++) {
                int doubleSplit = information[i].indexOf("=");
                information[i] = information[i].substring(doubleSplit + 1);
            }
            User user = new User(information[USER_ID], information[USER_NAME], information[USER_PASSWORD], information[USER_ID] + "@gmail.com");
            Database.addUser(user);
            User findUser = Database.findUserById(information[USER_ID]);
            logger.debug("FindUser : {}", findUser);

            uri = "/index.html";
        }
        return uri;
    }

    public static File getFile(String uri) {
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        return new File("./src/main/resources/static" + uri);
    }

}
