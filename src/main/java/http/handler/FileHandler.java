package http.handler;

import http.request.FilePath;
import http.response.ContentType;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import session.SessionManager;
import utils.StaticFileReader;

public class FileHandler extends CommandHandler {
    private static final String ACCOUNT_REGEX = "(<p class=\"post__account__nickname\">).*?(</p>)";

    @Override
    public void handleGetRequest() {
        FilePath filePath = requestManager.getFilePath();
        File file = filePath.makeFile();
        StaticFileReader staticFileReader = new StaticFileReader(file);
        ContentType contentType = getContentType(filePath);
        byte[] responseBody = verifyResponseBody(staticFileReader);

        responseManager.setOkResponse(contentType, responseBody);
    }

    private byte[] verifyResponseBody(StaticFileReader staticFileReader) {
        SessionManager sessionManager = new SessionManager(requestManager);
        String content = staticFileReader.readAsStr();

        Matcher accountMatcher = Pattern.compile(ACCOUNT_REGEX).matcher(content);
        if (sessionManager.isAuthorizedUser() && accountMatcher.find()) {
            String userName = sessionManager.getUserName();
            String replacement = accountMatcher.replaceAll("$1" + userName + "$2");
            return replacement.getBytes();
        }
        return staticFileReader.readAllBytes();
    }
}
