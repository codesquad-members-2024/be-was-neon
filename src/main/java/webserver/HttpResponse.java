package webserver;

import Utils.FileUtils;
import Utils.RouteManager;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final HttpResponseWriter out;
    private final byte[] body;
    private final String contentType;

    public HttpResponse(HttpResponseWriter out, String filePath) throws IOException {
        this.out = out;
        this.body = FileUtils.readFileContent(filePath);
        this.contentType = RouteManager.getContentType(filePath);
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

    public static void sendRedirect(HttpResponseWriter out, String location) throws IOException{
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        dos.writeBytes("\r\n");
    }
}