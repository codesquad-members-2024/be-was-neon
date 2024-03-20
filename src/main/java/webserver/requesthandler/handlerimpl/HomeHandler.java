package webserver.requesthandler.handlerimpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.User;
import webserver.requesthandler.http.ContentType;
import webserver.requesthandler.http.HttpRequest;
import webserver.requesthandler.http.HttpResponse;
import webserver.requesthandler.URLConst;
import webserver.requesthandler.session.SessionManager;

public class HomeHandler implements RequestHandler {

    public static final Pattern LOGGED_IN_USER_NICKNAME_PATTERN = Pattern.compile(
            "<p class=\"post__account__nickname\">.*?</p>");

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        User user = (User) SessionManager.findSession(request);
        if (user == null) { // 로그인 안된 사용자
            setHTMLToBody(response, URLConst.HOME_URL);
            return;
        }
        setHTMLToBodyByUserId(response, user);
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) {
        response.setNotFound();
    }

    private void setHTMLToBodyByUserId(HttpResponse response, User user) throws IOException {
        setHTMLToBody(response, URLConst.LOGGED_IN_USER_HOME_URL);
        String content = new String(response.getBody(), StandardCharsets.UTF_8);

        Matcher matcher = LOGGED_IN_USER_NICKNAME_PATTERN.matcher(content);
        byte[] modifiedBody = matcher.replaceAll("<p class=\"post__account__nickname\">" + user.getUserId() + "</p>")
                .getBytes(); // html의 account 부분을 아이디로 변경
        response.setBody(modifiedBody, ContentType.HTML);
    }
}
