package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class RequestManager {
    private static final Logger logger = LoggerFactory.getLogger(RequestManager.class);
    private InputStream in;
    HttpRequest httpRequest;

    public RequestManager(InputStream in){
        this.in = in;
        this.httpRequest = new HttpRequest();
    }

    public void readRequestMessage() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        readStartLine(br);
        readHeaders(br);
        if(httpRequest.isContentLengthExist()){
            readBody(br);
        }
    }

    private void readStartLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        httpRequest.storeStartLineData(line); // start line 저장
        logger.debug("request line : {}", httpRequest.getStartLine());
    }

    private void readHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while(!line.isEmpty()){ // 나머지 headers 순회
            httpRequest.storeHeadersData(line); // header 저장
            logger.debug("request header : {}", line);
            line = br.readLine();
        }
    }

    private void readBody(BufferedReader br) throws IOException {
        char[] buffer = new char[httpRequest.getContentLength()];
        br.read(buffer); // context length 만큼 읽기
        httpRequest.storeBodyData(new String(buffer)); // body 저장
    }
}