package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpRequestReader {
    private final BufferedReader reader;

    public HttpRequestReader(Socket connection) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    // 전체 HTTP 요청을 읽어서 문자열로 반환하는 새로운 메소드
    public String readRequest() throws IOException {
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }
        return requestBuilder.toString();
    }
}
