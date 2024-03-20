package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    public HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        RequestLineParser requestLineParser = new RequestLineParser();
        RequestLine requestLine = requestLineParser.parse(br.readLine());
        Map<String, String> requestHeader = parseRequestHeader(br);
        String requestBody = parseRequestBody(br, requestHeader);

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    private Map<String,String> parseRequestHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        Map<String,String>requestHeader = new HashMap<>();
        while (line != null && !line.isEmpty()) {
            String[] headerPieces = line.split(":", 2);
            requestHeader.put(headerPieces[0].trim(), headerPieces[1].trim());
            line = br.readLine();
        }
        return requestHeader;
    }

    private String parseRequestBody(BufferedReader br, Map<String,String> requestHeader) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        if(requestHeader.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(requestHeader.get("Content-Length"));
            char[] buffer = new char[contentLength];
            int bytesRead = br.read(buffer, 0, contentLength);
            if (bytesRead != -1) {
                requestBody.append(buffer, 0, bytesRead);
            }
        }
        return requestBody.toString();
    }
}
