package webserver.requesthandler.handlerimpl;

import db.Database;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import model.User;
import webserver.http.ContentType;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.requesthandler.url.URLConst;

public class UserListHandler implements RequestHandler {

    public static final String USER_TABLE_FORMAT = "<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>";
    public static final String TARGET_PART = "<!-- UserList -->";

    @Override
    public void handleGet(HttpRequest request, HttpResponse response) throws IOException {
        String template = getTemplate(response);
        List<User> allUsers = Database.findAll();

        byte[] modifiedBody = getModifiedBody(allUsers, template);
        response.setBody(modifiedBody, ContentType.HTML);
    }

    @Override
    public void handlePost(HttpRequest request, HttpResponse response) throws IOException {
        response.setNotFound();
    }

    private String getTemplate(HttpResponse response) throws IOException {
        setHTMLToBody(response, URLConst.USER_LIST_URL);
        return new String(response.getBody());
    }

    private byte[] getModifiedBody(List<User> allUsers, String template) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            sb.append(String.format(USER_TABLE_FORMAT,
                    i + 1, user.getUserId(), user.getName(), user.getEmail()));
        }

        return template.replace(TARGET_PART, sb).getBytes(StandardCharsets.UTF_8);
    }
}
