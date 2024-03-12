package webserver;

import db.Database;
import model.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Response {
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
        response302(dos);
    }

    static void responseGetFile(DataOutputStream dos, String url, FileType fileType) throws IOException {
        try {
            byte[] body = getFileBytes(url);
            response200Header(dos, body.length, fileType.getContentType());
            responseBody(dos, body);
        } catch (IOException | NullPointerException noSuchFile) { // 해당 경로의 파일이 없을 때
            response404(dos);
        }
    }

    private static byte[] getFileBytes(String url) throws IOException {
        File file = new File("./src/main/resources/static" + url); // 프로젝트 기준 상대 경로
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }catch (IOException e){
            System.out.println("noSuchFile");
        }
        return bytes;
    }

    private static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private static void response302(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location:" + "/index.html");
        dos.writeBytes("\r\n");
    }

    private static void response404(DataOutputStream dos) throws IOException {
        String errorMessage = "404 Not Found";
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/plain\r\n");
        dos.writeBytes("Content-Length: " + errorMessage.length() + "\r\n");
        dos.writeBytes("\r\n");
        dos.writeBytes(errorMessage); // body
    }
}
