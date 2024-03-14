package util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HeaderPrinter {
    public static void printRequestHeader(BufferedReader br, String httpRequestHeader) {

        Logger log = LoggerFactory.getLogger(RequestHandler.class);
        log.debug(httpRequestHeader);

        while (!httpRequestHeader.isEmpty()) {
            try {
                httpRequestHeader = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.debug(httpRequestHeader);
        }
    }
}
