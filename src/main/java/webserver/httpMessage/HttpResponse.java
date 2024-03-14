package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.TypeMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final List<String> header;
    private final byte[] body;

    private HttpResponse(File file) {
        header = getHeader(TypeMapper.getContentType(file), (int) file.length());
        body = readFile(file);
    }

    public static HttpResponse from(File file) {
        return new HttpResponse(file);
    }

    private byte[] readFile(File file) {
        byte[] body = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return body;
    }

    public List<String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    private List<String> getHeader(String contentType, int bodyLength) {
        List<String> header = new ArrayList<>();

        header.add("HTTP/1.1 200 OK \r\n");
        header.add("Content-Type: " + contentType + ";charset=utf-8\r\n");
        header.add("Content-Length: " + bodyLength + "\r\n");
        header.add("\r\n");

        return header;
    }
}
