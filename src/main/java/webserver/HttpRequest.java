package webserver;

import Utils.HttpRequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private Socket connection;
    private String httpRequest;
    private String path;

    public HttpRequest(Socket connection) throws IOException {
        this.connection = connection;
        parseRequest();
    }

    private void parseRequest() throws IOException {
        InputStream in = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        StringBuilder requestBuilder = new StringBuilder();
        String line;
        boolean isFirstLine = true;
        while (!(line = br.readLine()).isEmpty()) {
            if (isFirstLine) {
                logger.info("요청 라인: {}", line);
                isFirstLine = false;
            }
            requestBuilder.append(line).append("\r\n");
        }
        httpRequest = requestBuilder.toString();
        HttpRequestParser httpRequestParser = new HttpRequestParser(httpRequest);
        path = httpRequestParser.extractPath();
    }

    public String getPath() {
        return path;
    }
}
