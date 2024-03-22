package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Parser;
import util.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private String startRequest;
    private Map<String, String> requestHeader = new HashMap<>();

    public HttpRequest(BufferedReader br) throws IOException {
        init(br);
    }

    private void init(BufferedReader br) throws IOException {
        startRequest = br.readLine();
        String tempRequest = br.readLine();
        while (!tempRequest.isEmpty()) {
            String[] headers = Parser.splitColon(tempRequest);
            requestHeader.put(headers[0], headers[1]);
            tempRequest = br.readLine();
        }
    }

    public String getTargetURI() {
        return Parser.getURI(startRequest);
    }

    public String getStartRequest() {
        return startRequest;
    }

}
