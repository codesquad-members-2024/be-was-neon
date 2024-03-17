package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private String requestMethod;
    private String requestType;
    private Map<String, String> requestHeader;

    public void parseRequestLines(InputStream in){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String requestLine = br.readLine();
            requestMethod = getRequestMethod(requestLine);
            requestType = getURL(requestLine);
            getRequestHeader(br);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getURL(String requestLine){
        // /index.html HTTP/1.1 string 형태로 받아와서 파일 이름을 사용할수있게 split 을 이용하여 index.html 형태로 파싱해줍니다.
        String[] getFile = requestLine.split(" ");
        logger.debug("Request : {}", requestLine);
        return getFile[1];
    }
    public String getRequestMethod (String requestLine){
        String[] getFile = requestLine.split(" ");
        return getFile[0];
    }
    public void getRequestHeader(BufferedReader in) {
        try {
            // StringBuilder 를 통해 모든 request header 를 합치고 parseHeaders 로 전달해줍니다.
            StringBuilder request = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                request.append(line).append("\r\n");
            }
            requestHeader = parseHeader(request.toString());
            // 모든 map 에 들어있는 헤더를 logger 를 이용하여 출력해줍니다.
            for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
                logger.debug("Header: {}: {}", entry.getKey(), entry.getValue());
            }

        } catch (IOException e) {
            logger.error("Error handling request: {}", e.getMessage());
        }
    }
    public Map<String, String> parseHeader(String request) {
        // 모든 request header 의 정보가 담긴 문자열을 받아와서 키와 값으로 나눠 Map 으로 반환해주었습니다.
        Map<String, String> headers = new HashMap<>();
        String[] lines = request.split("\r\n");
        for (String line : lines) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                headers.put(parts[0], parts[1]);
            }
        }
        return headers;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestType() {
        return requestType;
    }
    public Map<String,String> getRequestHeader() {
        return requestHeader;
    }
}
