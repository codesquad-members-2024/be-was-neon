package webserver;

import Utils.FileUtils;
import Utils.RouteManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {
    private OutputStream out;
    private byte[] body;
    private String contentType;

    public HttpResponse(OutputStream out, String filePath) throws IOException {
        this.out = out;
        this.body = FileUtils.readFileContent(filePath);
        this.contentType = RouteManager.getContentType(filePath); // 변경된 부분
        sendResponse();
    }

    private void sendResponse() throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: " + contentType + "\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
