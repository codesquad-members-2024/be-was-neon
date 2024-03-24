package webserver.html;

import model.User;

import java.io.*;
import java.util.Collection;

import static webserver.handler.Path.*;

public class DynamicHtml {

    public static final String USER_NAME = "user_name_position";

    public static String getLoginIndexHtml(User user) {
        StringBuilder sb = new StringBuilder();

        File file = new File(LOGIN_HOME_PAGE.getRelativePath());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(USER_NAME)) {
                    line = line.replace(USER_NAME, user.getName());
                }
                System.out.println(line);
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    public static String getUserListHtml(Collection<User> users) {
        StringBuilder sb = new StringBuilder();

        File file = new File(USER_LIST_PAGE.getRelativePath());

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                if (line.contains("user-list")) {
                    appendUsers(users, sb);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    private static void appendUsers(Collection<User> users, StringBuilder sb) {
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("</tr>");
        }
    }
}
