package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Parser {

    private static final String SPACE = " ";

    public static String getRequestTarget(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String startLine = br.readLine();

        String[] tokens = startLine.split(SPACE);

        String requestTarget = tokens[1];

        return requestTarget;
    }
}
