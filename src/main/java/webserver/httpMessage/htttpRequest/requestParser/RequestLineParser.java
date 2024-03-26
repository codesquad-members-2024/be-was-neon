package webserver.httpMessage.htttpRequest.requestParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Reader;
import webserver.MainRequestHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static util.constants.Delimiter.BLANK;
import static webserver.httpMessage.HttpConstants.*;

public class RequestLineParser {
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

    public Map<String, String> getParseResultFrom(BufferedInputStream bis) {
        Map<String, String> requestLine = new HashMap<>();
        try{
            String requestLineString = Reader.readLineFrom(bis);
            String[] requestLineComponents = requestLineString.split(BLANK);
            requestLine.put(METHOD, requestLineComponents[0]);
            requestLine.put(REQUEST_TARGET, requestLineComponents[1]);
            requestLine.put(HTTP_VERSION_KEY, requestLineComponents[2]);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return requestLine;
    }
}
