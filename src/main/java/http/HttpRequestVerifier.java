package http;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestVerifier {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestVerifier.class);
    private static final int METHOD_INDEX = 0;
    private static final int VERSION_INDEX = 2;
    private static final String VERSION_DELIMITER = "/";
    private static final int VERSION_NUMBER_INDEX = 1;
    private static final String METHOD_ERROR_MESSAGE = "HttpMethod가 올바르지 않습니다.";
    private static final String VERSION_ERROR_MESSAGE = "HttpVersion이 올바르지 않습니다.";

    public static boolean verify(String[] requestHeader) {
        if (!verifyMethod(requestHeader[METHOD_INDEX])) {
            logger.error(METHOD_ERROR_MESSAGE);
            return false;
        }
        if (!verifyVersion(requestHeader[VERSION_INDEX])) {
            logger.error(VERSION_ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static boolean verifyMethod(String method) {
        return Arrays.stream(HttpMethod.values()).anyMatch(httpMethod -> httpMethod.name().equals(method));
    }

    private static boolean verifyVersion(String version) {
        String versionNum = version.split(VERSION_DELIMITER)[VERSION_NUMBER_INDEX];
        return Arrays.stream(HttpVersion.values()).anyMatch(httpVersion -> httpVersion.getVersion().equals(versionNum));
    }
}
