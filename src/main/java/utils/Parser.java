package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
public class Parser {
    private static final String DEFAULT_URL = "/index.html";
    private static final String SPACE = " ";
    public static String getRequestTarget(InputStream in, Logger logger) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        //Http Request 는 BlankLine 포함된다.
        String startLine = br.readLine();
        logger.debug("startLine : {}",startLine);

        while(true)
        {
            String line = br.readLine();
            logger.debug("line : {}",line);
            if(line.isEmpty()) break;
        }

        String[] tokens = startLine.split(SPACE);

        String url = tokens[1];

        if (url.equals("/")) {
            url = DEFAULT_URL;
        }

        return url;
    }
}
