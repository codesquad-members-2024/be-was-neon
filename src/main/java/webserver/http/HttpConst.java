package webserver.http;

public interface HttpConst {
    String QUERY_COMMAND_START = "?";
    String PARAMS_DELIMITER = "&";
    String PARAM_DELIMITER = "=";
    String CONTENT_LENGTH_LABEL = "Content-Length";
    String CONTENT_TYPE_LABEL = "Content-Type";
    String EMPTY_STRING = "";
    String HEADER_DELIMITER = ":";
    int PARAMS_LENGTH = 2;
    String HTML_FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    String HTTP_VERSION = "HTTP/1.1 ";
    String CRLF = "\r\n";
    String START_LINE_DELIMITER = " ";
    String COOKIE_VALUE_DELIMITER = ";";
    String SPACE_REGEX = "\\s";

}
