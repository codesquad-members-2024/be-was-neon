package webserver.httpMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final List<String> httpRequest;
    private final String startLine;

    public HttpRequest(List<String> httpRequest) {
        validate(httpRequest);
        this.httpRequest = httpRequest;
        this.startLine = httpRequest.get(0);
    }

    private void validate(List<String> httpRequest) {
        if (httpRequest.isEmpty()) {
            throw new IllegalArgumentException("빈 HTTP 요청입니다.");
        }
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        List<String> httpRequest = new ArrayList<>();

        String line;
        while (!(line = br.readLine()).isEmpty()) {
            httpRequest.add(line);
        }

        return new HttpRequest(httpRequest);
    }

    public void read() {
        for (String line : httpRequest) {
            logger.debug("line = " + line);
        }
    }

    public String getRequestTarget() {
        String[] split = startLine.split(" ");
        return split[1];

    }
}
