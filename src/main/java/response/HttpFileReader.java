package response;

import model.User;

import java.io.*;
import java.util.Collection;

public class HttpFileReader {
    public static byte[] setBodyDefault(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString().getBytes("UTF-8");
    }
    public static byte[] setBodyLoginSuccess(String filePath, String userName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("%replace_username%")){
                    sb.append(line.replace("%replace_username%",userName)).append("\n");
                }else {
                    sb.append(line).append("\n");
                }
            }
        }
        return sb.toString().getBytes("UTF-8");
    }
    public static byte[] setBodyUserList(String filePath, Collection<User> users) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("%user_list%")){
                    appendUsers(users,sb);
                }else {
                    sb.append(line).append("\n");
                }
            }
        }
        return sb.toString().getBytes("UTF-8");
    }
    private static void appendUsers(Collection<User> users, StringBuilder sb) {
        for (User user : users) {
            sb.append("<tr>" + "\n");
            sb.append("<td>").append(user.getUserId()).append("</td>" + "\n");
            sb.append("<td>").append(user.getName()).append("</td>" + "\n");
            sb.append("</tr>" + "\n");
        }
    }
    public static byte[] stringToBytes(String htmlString) {
        try {
            return htmlString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
