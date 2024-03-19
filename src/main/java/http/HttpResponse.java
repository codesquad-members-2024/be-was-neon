package http;

import java.io.DataOutputStream;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String SP = " ";
    private static final String CRLF = "\r\n";
    private static final String SPLITTER = ";";
    public static final String EQUAL = "=";
    private String contentType = "Content-Type: ";
    private String charset = "charset=";
    private String contentLength = "Content-Length: ";
    private String lastModified = "Last-Modified: ";
    private String setCookie = "Set-Cookie: ";
    private String location = "Location: ";
    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public HttpResponse setHttpVersion(String httpVersion) {
        writeString(httpVersion + SP);
        return this;
    }

    public HttpResponse setStatusCode(HttpStatus status) {
        writeString(status.code + SP + status.message + CRLF);
        return this;
    }

    public HttpResponse setContentType(String contentType) {
        this.contentType += contentType;
        writeString(this.contentType + SPLITTER + SP);
        return this;
    }

    public HttpResponse setCharset(String charset) {
        writeString(this.charset + charset + CRLF);
        return this;
    }

    public HttpResponse setContentLength(int contentLength) {
        this.contentLength += contentLength;
        writeString(this.contentLength + CRLF);
        return this;
    }

    public HttpResponse setLastModified(LocalDateTime lastModified) {
        this.lastModified += lastModified.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        writeString(this.lastModified + CRLF);
        return this;
    }

    public HttpResponse setSetCookie(String key, String value) {
        this.setCookie += key + EQUAL + value + SPLITTER;
        writeString(this.setCookie + CRLF);
        return this;
    }

    public HttpResponse setLocation(String location) {
        this.location += location;
        writeString(this.location + CRLF);
        return this;
    }

    public HttpResponse setMessageBody(String stringMessageBody) {
        writeString(CRLF);
        writeString(stringMessageBody + CRLF);
        return this;
    }

    public HttpResponse setMessageBody(byte[] bytesMessageBody) {
        writeString(CRLF);
        writeBytes(bytesMessageBody);
        writeString(CRLF);
        return this;
    }

    public void flush() {
        try {
            dos.flush();
        } catch (IOException e) {
            logger.error("[RESPONSE ERROR] flush error: {}", e.getMessage());
        }
    }

    private void writeString(String string) {
        try {
            dos.writeBytes(string);
        } catch (IOException e) {
            logger.error("[RESPONSE ERROR]: {}", e.getMessage());
        }
    }

    private void writeBytes(byte[] bytes) {
        try {
            dos.write(bytes);
        } catch (IOException e) {
            logger.error("[RESPONSE ERROR]: {}", e.getMessage());
        }
    }
}
