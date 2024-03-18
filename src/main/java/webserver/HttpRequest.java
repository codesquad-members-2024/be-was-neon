package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import utils.Parser;

public class HttpRequest {

    private static final String QUESTION_MARK = "\\?";
    private static final String QUERY_STRING_DELIM = "&";
    private static final String KEY_VALUE_DELIM = "=";

    public static String getRequestTarget(InputStream in) throws IOException {

        String requestTarget = Parser.getRequestTarget(in);

        return requestTarget;
    }

    public static String getPath(String requestTarget) throws IOException {
        if(!requestTarget.contains(QUESTION_MARK)) return requestTarget;
        return requestTarget.substring(0,requestTarget.indexOf(QUESTION_MARK));
    }

    public static Map<String, String> getQueryString(String requestTarget) {
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

//    public static void showHttpRequest(InputStream in, Logger logger) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//        //Http Request 는 BlankLine 포함된다.
//        while (true) {
//            String line = br.readLine();
//            logger.debug("line : {}", line);
//            if (line.isEmpty()) {
//                break;
//            }
//        }
//    }

}
