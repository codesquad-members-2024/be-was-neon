package request;

public class RequestLineParser {
    public RequestLine parse(String requestLine){
        String[] getRequestTokens = requestLine.split(" ");
        String requestMethod =  getRequestTokens[0];
        String requestPath = getRequestTokens[1];
        String requestProtocol = getRequestTokens[2];
        return new RequestLine(requestMethod,requestPath,requestProtocol);
    }
}
