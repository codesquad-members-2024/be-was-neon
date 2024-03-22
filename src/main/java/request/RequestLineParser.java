package request;

import java.util.Map;

public class RequestLineParser {
    public RequestLine parse(String requestLine){
        String[] getRequestTokens = requestLine.split(" ");
        String requestMethod =  getRequestTokens[0];
        String requestPath = parsePath(getRequestTokens[1]);
        String queryParam = parseQueryParam(getRequestTokens[1]);
        String requestProtocol = getRequestTokens[2];
        return new RequestLine(requestMethod,requestPath,queryParam,requestProtocol);
    }
    private String parsePath(String path) {
        if (path.contains("?")) {
            return path.substring(0, path.indexOf("?"));
        }
        return path;
    }
    private String parseQueryParam(String path) {
        if (path.contains("?"))
            return path.substring(path.indexOf("?") + 1);
        return "";
    }
}
