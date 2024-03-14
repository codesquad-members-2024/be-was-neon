package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import utils.Parser;

public class HttpRequest {


    public static String getRequestTarget(InputStream in, Logger logger) throws IOException {

        String requestTarget = Parser.getRequestTarget(in, logger);

        return requestTarget;
    }

    public static void showHttpRequest(InputStream in, Logger logger) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        //Http Request 는 BlankLine 포함된다.
        while(true)
        {
            String line = br.readLine();
            logger.debug("line : {}",line);
            if(line.isEmpty()) break;
        }
    }
}
