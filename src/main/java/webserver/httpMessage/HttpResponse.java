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

    public HttpResponse(List<String> header) {
        this.header = header;
        this.body = new byte[]{};
    }

    public HttpResponse(List<String> header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public static HttpResponse from(File file) {
        List<String> header = getHeader(TypeMapper.getContentType(file), (int) file.length());
        byte[] body = readFile(file);
        return new HttpResponse(header, body);
    }

    public static HttpResponse redirect(String location) {
        return new HttpResponse(getRedirectHeader(location));
    }

    private static byte[] readFile(File file) {
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

    private static List<String> getHeader(String contentType, int bodyLength) {
        List<String> header = new ArrayList<>();

        header.add(HttpStatus.OK.getStatusMessage());
        header.add("Content-Type: " + contentType + ";charset=utf-8 \r\n");
        header.add("Content-Length: " + bodyLength + " \r\n");
        header.add("\r\n");

        return header;
    }

    private static List<String> getRedirectHeader(String location) {
        List<String> header = new ArrayList<>();

        header.add(HttpStatus.TEMPORARY_REDIRECTION.getStatusMessage());
        header.add("Location: " + location + " \r\n");
        header.add("\r\n");

        return header;
    }

    public void send(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            writeHeader(getHeader(), dos);
            writeBody(getBody(), dos);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeBody(byte[] body, DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }

    private void writeHeader(List<String> httpResponse, DataOutputStream dos) throws IOException {
        for (String line : httpResponse) {
            dos.writeBytes(line);
        }
    }
}
