package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private static final String STATIC_PATH = "src/main/resources/static";
    private static final String INDEX_HTML = "/index.html";

    private final DataOutputStream dos;
    private final String filePath;

    private HttpResponse(DataOutputStream dos, String requestTarget) {
        this.dos = dos;
        filePath = getFilePath(requestTarget);
    }

    public static HttpResponse from(OutputStream out, String requestTarget) {
        DataOutputStream dos = new DataOutputStream(out);
        return new HttpResponse(dos, requestTarget);
    }

    private byte[] getBody(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] body = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        }
        return body;
    }

    private String getFilePath(String requestTarget) {
        if (isDirectory(requestTarget)) {
            return STATIC_PATH + requestTarget + INDEX_HTML;
        } else {
            return STATIC_PATH + requestTarget;
        }
    }

    private static boolean isDirectory(String requestTarget) {
        return !requestTarget.contains(".");
    }

    public void send() throws IOException {
        byte[] body = getBody(filePath);

        response200Header(body.length);
        responseBody(body);
    }

    private String getFileName(String filePath) {
        String[] splitFilePath = filePath.split("/");
        return splitFilePath[splitFilePath.length - 1];
    }

    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + getContentType() + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType() {
        String fileName = getFileName(filePath);
        String extension = getExtension(fileName);

        return switch (extension) {
            case "html", "css" -> "text/" + extension;
            case "svg" -> "image/svg+xml";
            case "ico" -> "image/x-icon";
            default -> throw new IllegalArgumentException("");
        };
    }

    private static String getExtension(String fileName) {
        String[] split = fileName.split("\\.");
        return split[split.length - 1];
    }
}
