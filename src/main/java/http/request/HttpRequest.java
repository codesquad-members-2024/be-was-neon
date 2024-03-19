package http.request;

import static utils.Constant.BLANK;
import static webserver.RequestHandler.logger;

import db.Database;
import http.Version;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import model.User;
import utils.Decoder;

public class HttpRequest {
    private static final int ID_INDEX = 0;
    private static final int PW_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int EMAIL_INDEX = 3;
    private static final int METHOD_INDEX = 0;
    private static final int FILE_PATH_INDEX = 1;
    private static final int VERSION_INDEX = 2;
    private static final String FIRST_LINE_MSG = "[HTTP Request] First Line : ";
    private static final String FIRST_LINE = "%s %s %s\n";
    private static final String HEADERS_MSG = "[HTTP Request] headers : \n";
    private static final String HEADERS = "%s %s\n";

    private Method method;
    private FilePath filePath;
    private Version version;
    private RequestHeaders requestHeaders;
    private HttpRequestBody body;

    public void setFirstLine(String firstLine) throws FileNotFoundException {
        String[] lines = firstLine.split(BLANK);
        this.method = new Method(lines[METHOD_INDEX]);
        this.filePath = new FilePath(lines[FILE_PATH_INDEX]);
        this.version = new Version(lines[VERSION_INDEX]);
    }

    public void setRequestHeaders(String line) {
        this.requestHeaders = new RequestHeaders(line);
    }

    public void setBody(String line) {
        this.body = new HttpRequestBody(line);
    }

    public String getFormattedRequest() {
        StringBuilder sb = new StringBuilder();
        sb.append(FIRST_LINE_MSG);
        sb.append(String.format(FIRST_LINE, method.getMethod(), filePath.getOriginalFilePath(), version.getVersion()));
        sb.append(HEADERS_MSG);
        sb.append(String.format(HEADERS, "Host", requestHeaders.findHeader("Host")));
        sb.append(String.format(HEADERS, "Cache-Control", requestHeaders.findHeader("Cache-Control")));
        sb.append(String.format(HEADERS, "User-Agent", requestHeaders.findHeader("User-Agent")));
        sb.append(String.format(HEADERS, "Accept-Language", requestHeaders.findHeader("Accept-Language")));
        sb.append(String.format(HEADERS, "Cookie", requestHeaders.findHeader("Cookie")));

        return sb.toString();
    }

    public Method getMethod() {
        return method;
    }

    public FilePath getFilePath() {
        return filePath;
    }

    public Optional<String> getContentLength() {
        return Optional.ofNullable(requestHeaders.findHeader("Content-Length"));
    }

    public void createUser() {
        String[] userEncodedInfo = body.extractUser();
        String[] userInfo = decodeUserInfo(userEncodedInfo);
        User user = generateUserObj(userInfo);
        Database.addUser(user);
        logger.info(user.toString());
    }

    private String[] decodeUserInfo(String[] userEncodedInfo) {
        try {
            return Decoder.decodeUser(userEncodedInfo);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return new String[0];
    }

    private User generateUserObj(String[] userInfo) {
        String id = userInfo[ID_INDEX];
        String pw = userInfo[PW_INDEX];
        String name = userInfo[NAME_INDEX];
        String email = userInfo[EMAIL_INDEX];
        return new User(id, pw, name, email);
    }
}