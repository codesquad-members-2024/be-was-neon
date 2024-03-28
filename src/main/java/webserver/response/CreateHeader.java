package webserver.response;

import java.util.ArrayList;
import java.util.List;

//200 302 404 코드를 생성해서 보냄

public class CreateHeader {
    private static final String OK_200 = "HTTP/1.1 200 OK \r\n";
    private static final String REDIRECTION_302 = "HTTP/1.1 302 FOUND\r\n";
    private static final String GENERATE_NOT_FOUND_404 = "HTTP/1.1 404 NOT FOUND\r\n";
    private static final String INTERNAL_SERVER_ERROR_500 = "HTTP/1.1 500 Internal Server Error\r\n";

    public List<String> generate200(int fileLength, String type) {
        List<String> headers = new ArrayList<>();
        headers.add(REDIRECTION_302);
        headers.add("Content-Type: " + type + ";charset=utf-8\r\n");
        headers.add("Content-Length: " + fileLength + "\r\n");
        headers.add("\r\n");
        return headers;
    }

    public List<String> redirect302(String location) {
        List<String> headers = new ArrayList<>();
        headers.add(REDIRECTION_302);
        headers.add("Location: " + location + "\r\n");
        headers.add("\r\n");
        return headers;
    }

    public List<String> notFound404() {
        List<String> headers = new ArrayList<>();
        headers.add(GENERATE_NOT_FOUND_404);
        headers.add("\r\n");
        return headers;
    }

    public List<String> serverError500() {
        List<String> headers = new ArrayList<>();
        headers.add(INTERNAL_SERVER_ERROR_500);
        headers.add("\r\n");
        return headers;
    }

    public List<String> loginSuccessWithCookie(String cookieValue) {
        List<String> headers = new ArrayList<>();
        headers.add(REDIRECTION_302);
        headers.add("Location: /main/index.html\r\n");
        headers.add("Set-Cookie: session=" + cookieValue+ "; Path=/\r\n");
        headers.add("Content-Type: text/html;charset=utf-8\r\n");
        headers.add("\r\n");
        return headers;
    }

}
