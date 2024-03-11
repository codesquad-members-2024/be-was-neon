package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHeaderParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHeaderParser.class);

    public static String requestParse(String line) {
        if (line.startsWith("GET")) {
            logger.debug(line);
            return line.split(" ")[1];
        } else if (line.startsWith("Host")) {
            logger.debug(line);
        } else if (line.startsWith("Connection")) {
            logger.debug(line);
        } else if (line.startsWith("Accept:")) {
            logger.debug(line);
        }
        return "";
    }
}
