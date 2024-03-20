package webserver.httpMessage;

import webserver.utils.HttpRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequestReader {

    public static final String UTF_8 = "UTF-8";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String EMPTY_BODY = "";

    private final BufferedReader br;

    public HttpRequestReader(InputStream in) {
        br = new BufferedReader(new InputStreamReader(in));
    }

    public HttpRequest readHttpRequest() throws IOException {
        String startLine = br.readLine();
        Map<String, String> header = readHeader();
        String body = readBody(header.get(CONTENT_LENGTH));
        return new HttpRequest(startLine, header, body);
    }

    private String readBody(String contentLength) throws IOException {
        if (contentLength == null) {
            return EMPTY_BODY;
        }

        return readBodyContent(contentLength);
    }

    private String readBodyContent(String contentLength) throws IOException {
        StringBuilder bodyBuffer = new StringBuilder();
        int length = Integer.parseInt(contentLength);
        for (int i = 0; i < length; i++) {
            bodyBuffer.append((char) br.read());
        }
        return URLDecoder.decode(bodyBuffer.toString(), UTF_8);
    }

    private Map<String, String> readHeader() throws IOException {
        List<String> requestHeader = new ArrayList<>();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            requestHeader.add(line);
        }
        return HttpRequestParser.parseHeader(requestHeader);
    }

}
