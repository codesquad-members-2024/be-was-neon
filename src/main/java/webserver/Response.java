package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static webserver.ResponseStatus.*;
import static webserver.WebServer.staticSourcePath;

public class Response {
    Map<String, Runnable> urlMapping = Map.of(
            "default", this::responseGet,
            "/create", this::createUser
    );
    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private byte[] header;
    private byte[] body;
    private String path;
    private final Map<String, String> params;
    private FileType fileType;

    public Response(Request request) {
        this.path = request.getPath();
        this.params = request.getParams();

        String urlKey = urlMapping.keySet().stream()
                .filter(url -> path.startsWith(url))
                .findFirst().orElse("default");
        urlMapping.get(urlKey).run();
    }

    public byte[] getHeader() {
        return header.clone();
    }

    public byte[] getBody() {
        return body.clone();
    }

    private void createUser() {
        User user = new User(params.get("name"), params.get("password"), params.get("nickname"), params.get("email"));
        Database.addUser(user);
        log.info("User Created : " + user.getUserId());
        writeResponseHeader(FOUND, FileType.NONE, 0);
    }

    private void responseGet() {
        try {
            body = getFileBytes();
            writeResponseHeader(OK, fileType, body.length);
        } catch (IOException noSuchFile) { // 해당 경로의 파일이 없을 때 getFileBytes 에서 예외 발생 , 로그 출력 후 던짐
            // 404 페이지 응답
            writeResponseHeader(NotFound, FileType.TXT, NotFound.getMessage().length());
            body = (NotFound.getMessage().getBytes());
        }
    }

    private byte[] getFileBytes() throws IOException {
        setFilePath();
        File file = new File(staticSourcePath + path); // 프로젝트 기준 상대 경로

        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        } catch (IOException e) {
            log.error("noSuchFile " + file.toPath());
            throw e;
        }

        log.info(file.getName() + " send");
        return fileBytes;
    }

    private void setFilePath() {
        try {
            // valueOf(~.toUpperCase()) 로 파일타입 Enum 에서 타입 찾아 지정
            String[] type = path.split("\\.");
            fileType = FileType.valueOf(type[type.length - 1].toUpperCase());
        } catch (IllegalArgumentException justPath) {
            // 파일이 아니라 경로라면 그 경로의 index.html 을 요청한 것으로 간주
            path = path + "/index.html";
            fileType = FileType.HTML;
        }
    }

    private void writeResponseHeader(ResponseStatus status, FileType contentType, int contentLength) {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 ").append(status.getMessage()).append("\r\n");
        if (status == FOUND) sb.append("Location:" + "/index.html"); // 이부분 파라미터로 사용하지 못해 고민
        else {
            sb.append("Content-Type: ").append(contentType.getContentType()).append("\r\n");
            sb.append("Content-Length: ").append(contentLength).append("\r\n");
            sb.append("\r\n");
        }

        header = sb.toString().getBytes();
    }
}
