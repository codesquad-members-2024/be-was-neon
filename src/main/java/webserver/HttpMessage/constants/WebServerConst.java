package webserver.HttpMessage.constants;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WebServerConst {

    // HTTP
    public static final String CRLF = "\r\n";
    
    // Version
    public static final String HTTP_VERSION = "HTTP/1.1";

    // HTTP Methods
    public static final String GET = "GET";
    public static final String POST = "POST";

    // startLine
    public static final String STARTLINE_DELIM = " ";

    // header fields
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LEN = "Content-Length";
    public static final String LOCATION = "Location";
    public static final String COOKIE = "Cookie";
    public static final String HEADER_DELIM = ": ";
    public static final String VALUE_DELIM = "; ";

    // queries
    public static final String QUERY_START = "\\?";
    public static final String QUERY_DELIM = "&";
    public static final String QUERY_VALUE_DELIM = "=";
    public static final String USER_ID = "userId";
    public static final String USER_PW = "password";

    // File
    public static final String EXTENDER_START = "\\.";

    // etc
    public static final String CHRACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // format
    public static final DateTimeFormatter DateTimeFORMAT = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);


    private WebServerConst(){}
}
