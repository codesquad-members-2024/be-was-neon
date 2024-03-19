package webserver.HttpHandler;

import db.Database;
import db.SessionStore;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMessage.*;
import webserver.Mapping.GetMapping;
import webserver.Mapping.PostMapping;
import webserver.eums.FileType;
import webserver.eums.ResponseStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static webserver.WebServer.staticSourcePath;
import static webserver.eums.ResponseStatus.*;

public class RequestHandler {
    ResponseStartLine startLine;
    MessageHeader responseHeader = new MessageHeader(new HashMap<>());
    MessageBody responseBody;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @PostMapping(path = "/create")
    public Response createUser(Request request) {
        MessageBody reqBody = request.getBody();

        try {
            User user = new User(
                    reqBody.getContentByKey("userId"),
                    reqBody.getContentByKey("password"),
                    reqBody.getContentByKey("name"),
                    reqBody.getContentByKey("email")
            );

            Database.addUser(user);
            log.info("User Created : " + user.getUserId());
        } catch (IllegalArgumentException fail) {
            log.info("Fail to create new user : " + fail.getMessage());
        }

        startLine = new ResponseStartLine("HTTP/1.1", FOUND);
        writeResponseHeader(FOUND, FileType.NONE, 0);

        return new Response(startLine).header(responseHeader);
    }

    @PostMapping(path = "/login")
    public Response login(Request request) {
        MessageBody requestBody = request.getBody();
        User user = Database.findUserById(requestBody.getContentByKey("userId"));

        try {
            if (user.getPassword().equals(requestBody.getContentByKey("password"))) {
                String cookie = responseHeader.addCookie(10);
                SessionStore.addSession(cookie, user, 60000);
                log.info("login : " + user.getName());

                responseHeader.addHeaderField("Location", "/");
            } else {
                log.info("login failed : password mismatch");
                responseHeader.addHeaderField("Location", "/");
            }
        } catch (NullPointerException notExistUser) {
            log.info("login failed : notExistUser");
            responseHeader.addHeaderField("Location", "/");
        }

        startLine = new ResponseStartLine("HTTP/1.1", FOUND);
        return new Response(startLine).header(responseHeader);
    }

    @PostMapping(path = "/logout")
    public Response logout(Request request) {
        SessionStore.removeSession(request.getHeaderValue("Cookie").split("sid=")[1]);
        log.info("logout");

        startLine = new ResponseStartLine("HTTP/1.1", FOUND);
        responseHeader.addHeaderField("Location", "/");
        return new Response(startLine).header(responseHeader);
    }

    @GetMapping(path = "/")
    public Response responseGet(Request request) {
        String path = request.getStartLine().getUri();
        path = verifySession(request, path);
        log.debug("path : " + path);

        File file = new File(getFilePath(path));
        log.debug("filepath : " + getFilePath(path));
        try {
            responseBody = new MessageBody(file);
            startLine = new ResponseStartLine("HTTP/1.1", OK);
            writeResponseHeader(OK, responseBody.getContentType(), responseBody.getContentLength());
        } catch (IOException noSuchFile) { // 해당 경로의 파일이 없을 때 getFileBytes 에서 예외 발생 , 로그 출력 후 던짐
            // 404 페이지 응답
            responseBody = new MessageBody(NotFound.getMessage(), FileType.TXT);
            startLine = new ResponseStartLine("HTTP/1.1", NotFound);
            writeResponseHeader(NotFound, responseBody.getContentType(), responseBody.getContentLength());
        }

        return new Response(startLine).header(responseHeader).body(responseBody);
    }

    private String verifySession(Request request, String path){
        User user;
        try {
            user = SessionStore.getSession(request.getHeaderValue("Cookie").split("sid=")[1]);
        }catch (NullPointerException noCookieSid){
            return path;
        }

        if(path.equals("/") &&  user != null){
            path = path + "/main"; // 로그인 된 세션의 사용자가 /로 접속하면 main/ 으로 보냄
            log.info("welcome Logged-in user " + user.getName());
        }
        return path;
    }

    private String getFilePath(String path) {
        String[] splitPath = path.split("/");
        if (splitPath.length == 0 || !splitPath[splitPath.length - 1].contains(".")) {
            // 파일이 아니라 경로라면 그 경로의 index.html 을 요청한 것으로 간주
            return staticSourcePath + path + "/index.html";
        }
        return staticSourcePath + path;
    }

    private void writeResponseHeader(ResponseStatus status, FileType contentType, long contentLength) {
        if (status == FOUND) responseHeader.addHeaderField("Location", "/index.html");

        else {
            responseHeader.addHeaderField("Content-Type", contentType.getMimeType());
            responseHeader.addHeaderField("Content-Length", contentLength + "");
        }
    }
}
