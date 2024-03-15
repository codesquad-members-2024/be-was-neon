package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
public class Parser {
    private static final String SPACE = " ";
    public static String getRequestTarget(InputStream in, Logger logger) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String startLine = br.readLine();
        logger.debug("startLine : {}",startLine);

        String[] tokens = startLine.split(SPACE);

        String requestTarget = tokens[1];

        return requestTarget;
    }
}
