package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpResponseWriter {
    private final DataOutputStream writer;

    public HttpResponseWriter(Socket connection) throws IOException {
        OutputStream output = connection.getOutputStream();
        this.writer = new DataOutputStream(output);
    }

    public void sendResponse(byte[] body, String contentType) throws IOException {
        response200Header(body.length, contentType);
        responseBody(body);
    }

    public void sendRedirect(String location) throws IOException {
        writer.writeBytes("HTTP/1.1 302 Found \r\n");
        writer.writeBytes("Location: " + location + "\r\n");
        writer.writeBytes("\r\n");
    }

    public void sendError404() throws IOException {
        writer.writeBytes("HTTP/1.1 404 Not Found\r\n");
        writer.writeBytes("Content-Type: text/html; charset=UTF-8\r\n");
        writer.writeBytes("\r\n");
        writer.writeBytes("<!DOCTYPE html>\n<html>\n<head>\n<title>404 Not Found\n</title>\n</head>\n<body>\n<h1>404 Not Found\n</h1>\n<h2>The requested page could not be found.\n</h2>\n</body>\n</html>");
    }

    private void response200Header(int lengthOfBodyContent, String contentType) throws IOException {
        writer.writeBytes("HTTP/1.1 200 OK \r\n");
        writer.writeBytes("Content-Type: " + contentType + "\r\n");
        writer.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        writer.writeBytes("\r\n");
    }

    private void responseBody(byte[] body) throws IOException {
        writer.write(body, 0, body.length);
        writer.flush();
    }
}
