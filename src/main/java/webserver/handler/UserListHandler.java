package webserver.handler;

import db.Database;
import model.User;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.util.Collection;

public class UserListHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        Collection<User> users = Database.findAll();

        HttpResponse response = new HttpResponse();
        response.setStatus(HttpStatus.OK);
        response.setBody(getUserListHtml(users).getBytes());

        return response;
    }

    private String getUserListHtml(Collection<User> users) {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("  <head>");
        sb.append("    <meta charset=\"UTF-8\" />");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
        sb.append("    <link href=\"../reset.css\" rel=\"stylesheet\" />");
        sb.append("    <link href=\"../global.css\" rel=\"stylesheet\" />");

        sb.append("<style>");
        sb.append("  table {");
        sb.append("    border-collapse: collapse;");
        sb.append("    border: 1px solid #ddd;");
        sb.append("    width: 100%;");
        sb.append("    margin: 0 auto;");
        sb.append("    text-align: center;");
        sb.append("    font-family: sans-serif;");
        sb.append("  }");
        sb.append("  th, td {");
        sb.append("    border: 1px solid #ddd;");
        sb.append("    padding: 8px;");
        sb.append("    font-size: 16px;");
        sb.append("    letter-spacing: 4px;");
        sb.append("  }");
        sb.append("  th {");
        sb.append("    background-color: #f2f2f2;");
        sb.append("  }");
        sb.append("  tr:nth-child(even) {");
        sb.append("    background-color: #fafafa;");
        sb.append("  }");
        sb.append("</style>");

        sb.append("  </head>");
        sb.append("  <body>");
        sb.append("    <div class=\"container\">");
        sb.append("      <header class=\"header\">");
        sb.append("        <a href=\"/\"><img src=\"../img/signiture.svg\" /></a>");
        sb.append("        <ul class=\"header__menu\">");
        sb.append("          <li class=\"header__menu__item\">");
        sb.append("            <a class=\"btn btn_contained btn_size_s\" href=\"/login/index.html\">로그인</a>");
        sb.append("          </li>");
        sb.append("          <li class=\"header__menu__item\">");
        sb.append("            <a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">");
        sb.append("              회원 가입");
        sb.append("            </a>");
        sb.append("          </li>");
        sb.append("        </ul>");
        sb.append("      </header>");
        sb.append("      <div class=\"page\">");
        sb.append("        <h2 class=\"page-title\">유저 정보</h2>");


        sb.append("<table>");

        sb.append("<thead>");
        sb.append("    <tr>");
        sb.append("      <th> 사용자 ID </th>");
        sb.append("      <th> 이름 </th>");
        sb.append("      <th> 이메일 </th>");
        sb.append("    </tr>");
        sb.append("</thead>");

        sb.append("<tbody>");
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<td>").append(user.getUserId()).append("</td>");
            sb.append("<td>").append(user.getName()).append("</td>");
            sb.append("<td>").append(user.getEmail()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</tbody>");

        sb.append("</table>");

        sb.append("      </div>");
        sb.append("    </div>");
        sb.append("  </body>");
        sb.append("</html>");

        return sb.toString();
    }
}
