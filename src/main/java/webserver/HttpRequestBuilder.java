package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.HttpBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestBuilder {

    private static final String QUESTION_MARK = "?";
    private static final String QUERY_STRING_DELIM = "&";
    private static final String KEY_VALUE_DELIM = "=";
    private static final String SPACE = " ";
    private String[] requestLines;
    private int contentLength;
    private BufferedReader br;
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestBuilder.class);

    public HttpRequestBuilder(InputStream in) throws UnsupportedEncodingException {
        this.br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    }
    public String getHttpMethod() {
        String httpMethod = this.requestLines[0];

        return httpMethod;
    }
    public String getRequestTarget() {
        String requestTarget = this.requestLines[1];

        return requestTarget;
    }
    public String getHttpVersion() {
        String httpVersion = this.requestLines[2];

        return httpVersion;
    }
    private String[] getRequestLine(BufferedReader br) throws IOException {
        String[] requestLines = br.readLine().split(SPACE);
        return requestLines;
    }

    public String getPath() {
        String requestTarget = getRequestTarget();
        //정규표현식으로 하면 \\? <-> 정규표현식이 아니면 ?
        if(!requestTarget.contains(QUESTION_MARK)) return requestTarget;
        return requestTarget.substring(0,requestTarget.indexOf(QUESTION_MARK));
    }

    public Map<String, String> getQueryString() {
        String requestTarget =  getRequestTarget();
        HashMap<String, String> queryMap = new HashMap();

        //QUESTION_MARK 를 포함하고 있지 않으면 쿼리 스트링이 없으므로 빈 queryMap 리턴
        if(!requestTarget.contains(QUESTION_MARK)) return queryMap;

        String queryString = requestTarget.substring(requestTarget.indexOf(QUESTION_MARK) + 1);
        StringTokenizer tokenizer = new StringTokenizer(queryString, QUERY_STRING_DELIM);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split(KEY_VALUE_DELIM);
            queryMap.put(keyValue[0], keyValue[1]);
        }
        return queryMap;
    }

    public void readHttpRequest() throws IOException {
        //Http Request 는 BlankLine 포함된다.
        this.requestLines = getRequestLine(br);
        while (true) {
            String line = br.readLine();
            logger.debug("line : {}", line);
            if(line.startsWith("Content-Length")){
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    this.contentLength = Integer.parseInt(matcher.group());
                }
            }
            if (line == null || line.isEmpty()) {
                break;
            }
        }
    }

    public HttpBody getHttpBody() throws IOException {
        String key = "";
        String value = "";
        boolean flag = true;
        Map<String, String> keyValue = new HashMap<>();
        for (int index = 0; index < contentLength; index++) {
            int unicodeCodePoint = br.read();
            String bodyChar = String.valueOf(Character.toChars(unicodeCodePoint));
            if(flag && !bodyChar.equals("=") && !bodyChar.equals("&")) key += bodyChar;
            if(!flag && !bodyChar.equals("=") && !bodyChar.equals("&")) value += bodyChar;
            if (bodyChar.equals("=")) {
                flag = false;
            }
            if(bodyChar.equals("&")){
                keyValue.put(key, value);
                key = "";
                value = "";
                flag = true;
            }
        }
        keyValue.put(key, value);

        return new HttpBody(keyValue);
    }
}
