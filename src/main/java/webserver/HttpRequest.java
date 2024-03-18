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

    public static String[] getRequestTarget(InputStream in, Logger logger) throws IOException {

        String requestTarget = Parser.getRequestTarget(in, logger);

        String[] pathAndQuery = requestTarget.split(QUESTION_MARK);

        return pathAndQuery;
    }

    public static Map<String, String> getQueryString(String queryString) {
        StringTokenizer tokenizer = new StringTokenizer(queryString, QUERY_STRING_DELIM);
        HashMap<String, String> queryMap = new HashMap();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] keyValue = token.split(KEY_VALUE_DELIM);
            queryMap.put(keyValue[0], keyValue[1]);
        }
        return queryMap;
    }

    public static void showHttpRequest(InputStream in, Logger logger) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        //Http Request 는 BlankLine 포함된다.
        while (true) {
            String line = br.readLine();
            logger.debug("line : {}", line);
            if (line.isEmpty()) {
                break;
            }
        }
    }
}
