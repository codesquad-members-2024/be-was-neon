package webserver.HttpHandler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMessage.*;
import webserver.Mapping.GetMapping;
import webserver.eums.FileType;
import webserver.eums.ResponseStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static webserver.WebServer.staticSourcePath;
import static webserver.eums.ResponseStatus.*;

public class RequestHandler {
    ResponseStartLine startLine;
    MessageHeader header;
    MessageBody body;
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    @GetMapping(path = "/create")
    public Response createUser(Request request) {
        User user = new User(
                request.getRequestQuery("userId"),
                request.getRequestQuery("password"),
                request.getRequestQuery("name"),
                request.getRequestQuery("email")
        );

        Database.addUser(user);
        log.info("User Created : " + user.getUserId());

        startLine = new ResponseStartLine("HTTP/1.1", FOUND);
        writeResponseHeader(FOUND, FileType.NONE, 0);

        return new Response(startLine).header(header);
    }

    @GetMapping(path = "/")
    public Response responseGet(Request request) {
        String path = request.getStartLine().getUri();
        log.debug("path : " +path);

        File file = new File(getFilePath(path));
        log.debug("filepath : " + getFilePath(path));
        try {
            body = new MessageBody(file);
            startLine = new ResponseStartLine("HTTP/1.1", OK);
            writeResponseHeader(OK , body.getContentType() , body.getContentLength());
        } catch (IOException noSuchFile) { // 해당 경로의 파일이 없을 때 getFileBytes 에서 예외 발생 , 로그 출력 후 던짐
            // 404 페이지 응답
            body = new MessageBody(NotFound.getMessage(), FileType.TXT);
            startLine = new ResponseStartLine("HTTP/1.1", NotFound);
            writeResponseHeader(NotFound, body.getContentType(), body.getContentLength());
        }

        return new Response(startLine).header(header).body(body);
    }

    private String getFilePath(String path) {
        String[] splitPath = path.split("/");
        if (splitPath.length == 0 || !splitPath[splitPath.length-1].contains(".")) {
            // 파일이 아니라 경로라면 그 경로의 index.html 을 요청한 것으로 간주
             return staticSourcePath + path + "/index.html";
        }
        return staticSourcePath + path;
    }

    private void writeResponseHeader(ResponseStatus status, FileType contentType, long contentLength) {
        header = new MessageHeader(new HashMap<>());

        if (status == FOUND) header.addHeaderField("Location", "/index.html");

        else {
            header.addHeaderField("Content-Type", contentType.getMimeType());
            header.addHeaderField("Content-Length", contentLength + "");
        }

    }
}
