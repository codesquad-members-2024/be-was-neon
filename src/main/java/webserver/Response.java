package webserver;

import db.Database;
import model.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static webserver.ResponseStatus.*;

public class Response {
    private Response(){}
    public static String sendResponse(DataOutputStream dos, Request request) throws IOException {
        if (request.getReqDetail().equals("createUser")) {
            createUser(dos, request.getParams());
        } else if (request.getReqDetail().equals("getFile")) {
            responseGetFile(dos, request.getUrl(), request.getFileType());
        }

        return request.getLog() + " Complete";
    }

    static void createUser(DataOutputStream dos, List<String> params) throws IOException {
        User user = User.makeUser(params);
        Database.addUser(user);
        sendResponseHeader(dos, FOUND , FileType.NONE , 0);
    }

    static void responseGetFile(DataOutputStream dos, String url, FileType fileType) throws IOException {
        try {
            byte[] body = getFileBytes(url);
            sendResponseHeader(dos, OK, fileType, body.length);
            dos.write(body);
            dos.flush();
        } catch (IOException | NullPointerException noSuchFile) { // 해당 경로의 파일이 없을 때
            sendResponseHeader(dos , NotFound , FileType.TXT , NotFound.getMessage().length());
            dos.writeBytes(NotFound.getMessage());
        }
    }

    private static byte[] getFileBytes(String url) throws IOException {
        File file = new File("./src/main/resources/static" + url); // 프로젝트 기준 상대 경로
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (IOException e) {
            System.out.println("noSuchFile");
        }
        return bytes;
    }

    private static void sendResponseHeader(DataOutputStream dos, ResponseStatus status, FileType contentType, int contentLength) throws IOException {
        dos.writeBytes("HTTP/1.1 " + status.getMessage() + "\r\n");
        if(status == FOUND) dos.writeBytes("Location:" + "/index.html"); // 이부분 파라미터로 사용하지 못해 고민
        else {
            dos.writeBytes("Content-Type: " + contentType.getContentType() + "\r\n");
            dos.writeBytes("Content-Length: " + contentLength + "\r\n");
            dos.writeBytes("\r\n");
        }
    }
}
