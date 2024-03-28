package webserver.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/* 설정 관련 클래스 */
import config.Config;

/* 웹 서버의 HTTP 요청 객체 및 관련 클래스 */
import webserver.http.request.HttpRequest;
import webserver.http.request.message.HttpMethod;
import webserver.http.request.message.HttpRequestBody;
import webserver.http.request.message.HttpRequestHeader;
import webserver.http.request.message.HttpRequestStartLine;
import webserver.utils.HeaderUtils;

public class HttpRequestReader {
    private static final String NONE = "";
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestReader.class);

    private BufferedReader br;

    public HttpRequestReader(InputStream in) {
        try {
            this.br = new BufferedReader(new InputStreamReader(in, Config.getEncoding()));
        } catch (UnsupportedEncodingException e) {
            logger.error("[ERROR] 지원되지 않는 인코딩 에러 발생");
            this.br = new BufferedReader(new InputStreamReader(in));
        }
    }


    public HttpRequest getRequest() throws IOException {
        HttpRequestStartLine startLine = new HttpRequestStartLine(br.readLine());

        HttpRequestHeader header = new HttpRequestHeader(readHeader());

        HttpRequestBody body = new HttpRequestBody(readBody(startLine, header));
        return new HttpRequest(startLine, header, body);
    }

    private Map<String, String> readHeader() throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();

        StringBuilder headerLine = new StringBuilder(br.readLine());
        while (!headerLine.toString().isEmpty()) {
            headers.put(HeaderUtils.getHeaderKey(headerLine.toString()), HeaderUtils.getHeaderValue(headerLine.toString()));
            headerLine.replace(0, headerLine.length(), NONE);
            headerLine.append(br.readLine());
        }

        return headers;
    }

    private String readBody(HttpRequestStartLine startLine, HttpRequestHeader header) throws IOException {
        if (startLine.getHttpMethod().equals(HttpMethod.GET)) {
            return NONE;
        }

        int length = Integer.parseInt(header.getValue("Content-Length"));

        char[] body = new char[length];
        br.read(body, 0, length);
        return new String(body);
    }


}
