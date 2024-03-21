package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.TypeMapper;

import java.io.*;
import java.util.UUID;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String BLANK_HEADER = "";
    public static final String CRLF = "\r\n";

    private String header;
    private byte[] body;

    public HttpResponse() {
        header = BLANK_HEADER;
        body = new byte[0];
    }

    public HttpResponse(String header) {
        this.header = header;
        this.body = new byte[0];
    }

    public HttpResponse(String header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public void redirect(String path) {
        setStatus(HttpStatus.SEE_OTHER);
        setLocation(path);
    }

    public void setBody(File file) {
        body = readFile(file);
        setContentType(TypeMapper.getContentType(file));
        setContentLength(body.length);
    }

    public void setBody(String body) {
        this.body = body.getBytes();
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

    public byte[] getBody() {
        return body;
    }

    public void setContentType(String contentType) {
        header += "Content-Type: " + contentType + ";charset=utf-8" + CRLF;
    }

    public void setContentLength(int bodyLength) {
        header += "Content-Length: " + bodyLength + CRLF;
    }

    public void setLocation(String location) {
        header += "Location: " + location + CRLF;
    }

    public void setStatus(HttpStatus status) {
        header += status.getStatusMessage() + CRLF;
    }

    public void send(OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            writeHeader(getHeader(), dos);
            writeBlankLine(dos);
            writeBody(getBody(), dos);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHeader() {
        return header;
    }

    private static void writeBlankLine(DataOutputStream dos) throws IOException {
        dos.writeBytes(CRLF);
    }

    private void writeBody(byte[] body, DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }

    private void writeHeader(String header, DataOutputStream dos) throws IOException {
        dos.writeBytes(header);
    }

    public void setCookie(UUID uuid) {
        header += "Set-Cookie: sid=" + uuid + "; Path=/" + CRLF;
    }
}
