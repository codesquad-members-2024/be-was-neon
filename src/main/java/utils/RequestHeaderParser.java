package utils;

import static utils.RequestHeaderParser.ParseString.*;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHeaderParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestHeaderParser.class);
    private static final int URL_INDEX = 1;
    private static final UnaryOperator<String> REQUEST_URL_PARSE = header -> header.startsWith(GET.value)
            ? header.split(BLANK.value)[URL_INDEX] : EMPTY.value;
    private static final Consumer<String> TARGET_LOGGER = header -> {
        if (isTargetLogger(header)) {
            logger.debug("[REQUEST-HEADER] {}", header);
        }
    };

    public static String requestParse(String line) {
        TARGET_LOGGER.accept(line);

        return REQUEST_URL_PARSE.apply(line);
    }

    public enum ParseString {
        EMPTY(""),
        BLANK(" "),
        GET("GET"),
        HOST("Host"),
        CONNECTION("Connection"),
        ACCEPT("Accept:"),
        ;

        private final String value;

        ParseString(String value) {
            this.value = value;
        }

        public static boolean isTargetLogger(String header) {
            return Arrays.stream(values())
                    .filter(target -> !target.equals(EMPTY))
                    .map(target -> target.value)
                    .anyMatch(header::startsWith);
        }
    }
}
