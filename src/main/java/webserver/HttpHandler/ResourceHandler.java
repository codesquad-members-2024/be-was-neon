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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static webserver.WebServer.staticSourcePath;
import static webserver.eums.ResponseStatus.*;

public class ResourceHandler implements Handler{
    private ResponseStartLine startLine;
    private MessageHeader responseHeader = new MessageHeader(new HashMap<>());
    private MessageBody responseBody;

    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);

    @GetMapping(path = "/")
    public Response responseGet(Request request) {
        String path = request.getStartLine().getUri();

        if (verifySession(request) && path.equals("/")) {
            path = path + "/main"; // 로그인 된 세션의 사용자가 /로 접속하면 main/ 으로 보냄
            log.info("welcome Logged-in user ");
        }

        log.debug("path : " + path);
        File file = new File(getFilePath(path));
        log.debug("filepath : " + getFilePath(path));
        try {
            responseBody = new MessageBody(file);
            startLine = new ResponseStartLine("HTTP/1.1", OK);
        } catch (IOException noSuchFile) { // 해당 경로의 파일이 없을 때 getFileBytes 에서 예외 발생 , 로그 출력 후 던짐
            // 404 페이지 응답
            responseBody = new MessageBody(NotFound.getMessage(), FileType.TXT);
            startLine = new ResponseStartLine("HTTP/1.1", NotFound);
        }

        responseHeader = writeContentResponseHeader(responseBody);
        return new Response(startLine)
                .header(responseHeader)
                .body(responseBody);
    }

    private String getFilePath(String path) {
        String[] splitPath = path.split("/");
        if (splitPath.length == 0 || !splitPath[splitPath.length - 1].contains(".")) {
            // 파일이 아니라 경로라면 그 경로의 index.html 을 요청한 것으로 간주
            return staticSourcePath + path + "/index.html";
        }
        return staticSourcePath + path;
    }
}
