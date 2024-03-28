package response;

import model.User;

import java.io.*;
import java.util.Collection;

public class HttpFileReader {
    private static final String ENCODING = "UTF-8";

    public static byte[] setBodyDefault(String filePath) throws IOException {
        return readAndReplace(filePath, null);
    }

    public static byte[] setBodyLoginSuccess(String filePath, String userName) throws IOException {
        return readAndReplace(filePath, userName);
    }

    public static byte[] setBodyUserList(String filePath, Collection<User> users) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("%user_list%")) {
                    appendUsers(users, sb);
                } else {
                    sb.append(line).append("\n");
                }
            }
        }
        return sb.toString().getBytes(ENCODING);
    }

    private static byte[] readAndReplace(String filePath, String userName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (userName != null && line.contains("%replace_username%")) {
                    sb.append(line.replace("%replace_username%", userName)).append("\n");
                } else {
                    sb.append(line).append("\n");
                }
            }
        }
        return sb.toString().getBytes(ENCODING);
    }

    private static void appendUsers(Collection<User> users, StringBuilder sb) {
        for (User user : users) {
            sb.append("<tr>").append("\n");
            sb.append("<td>").append(user.getUserId()).append("</td>").append("\n");
            sb.append("<td>").append(user.getName()).append("</td>").append("\n");
            sb.append("</tr>").append("\n");
        }
    }

    public static byte[] stringToBytes(String htmlString) {
        try {
            return htmlString.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
