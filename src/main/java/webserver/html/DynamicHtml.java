package webserver.html;

import model.User;

import java.io.*;
import java.util.Collection;

import static webserver.handler.Path.USER_LIST_PAGE;

public class DynamicHtml {

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

    public static String getLoginIndexHtml(User user) {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("  <head>");
        sb.append("    <meta charset=\"UTF-8\" />");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
        sb.append("    <link href=\"./reset.css\" rel=\"stylesheet\" />");
        sb.append("    <link href=\"./global.css\" rel=\"stylesheet\" />");
        sb.append("    <link href=\"./main.css\" rel=\"stylesheet\" />");
        sb.append("  </head>");
        sb.append("  <body>");
        sb.append("    <div class=\"container\">");
        sb.append("      <header class=\"header\">");
        sb.append("        <a href=\"/\"><img src=\"./img/signiture.svg\" /></a>");
        sb.append("        <ul class=\"header__menu\">");
        sb.append("          <li class=\"header__menu__item\">");
        sb.append("            <a class=\"btn btn_ghost btn_size_s\">" + user.getName() + "</a>");

        sb.append("          <li class=\"header__menu__item\">");
        sb.append("            <a class=\"btn btn_ghost btn_size_s\" href=\"/article\">");
        sb.append("              글쓰기");
        sb.append("            </a>");
        sb.append("          </li>");

        sb.append("          <li class=\"header__menu__item\">");
        sb.append("            <a id=\"logout-btn\" class=\"btn btn_contained btn_size_s\" href=\"/logout\">");
        sb.append("              로그아웃");
        sb.append("            </a>");
        sb.append("          </li>");

        sb.append("        </ul>");
        sb.append("      </header>");
        sb.append("      <div class=\"wrapper\">");
        sb.append("        <div class=\"post\">");
        sb.append("          <div class=\"post__account\">");
        sb.append("            <img class=\"post__account__img\" />");
        sb.append("            <p class=\"post__account__nickname\">account</p>");
        sb.append("          </div>");
        sb.append("          <img class=\"post__img\" />");
        sb.append("          <div class=\"post__menu\">");
        sb.append("            <ul class=\"post__menu__personal\">");
        sb.append("              <li>");
        sb.append("                <button class=\"post__menu__btn\">");
        sb.append("                  <img src=\"./img/like.svg\" />");
        sb.append("                </button>");
        sb.append("              </li>");
        sb.append("              <li>");
        sb.append("                <button class=\"post__menu__btn\">");
        sb.append("                  <img src=\"./img/sendLink.svg\" />");
        sb.append("                </button>");
        sb.append("              </li>");
        sb.append("            </ul>");
        sb.append("            <button class=\"post__menu__btn\">");
        sb.append("              <img src=\"./img/bookMark.svg\" />");
        sb.append("            </button>");
        sb.append("          </div>");
        sb.append("          <p class=\"post__article\">");
        sb.append("            우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한");
        sb.append("            모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. 즉, 응답이");
        sb.append("            잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다.");
        sb.append("            우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다.");
        sb.append("            리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을");
        sb.append("            갖고, 확장성 이 있습니다. 이로 인해 개발이 더 쉬워지고 변경 사항을");
        sb.append("            적용하기 쉬워집니다. 이 시스템은 장애 에 대해 더 강한 내성을 지니며,");
        sb.append("            비록 장애가 발생 하더라도, 재난이 일어나기 보다는 간결한 방식으로");
        sb.append("            해결합니다. 리액티브 시스템은 높은 응답성을 가지며 사용자 에게");
        sb.append("            효과적인 상호적 피드백을 제공합니다.");
        sb.append("          </p>");
        sb.append("        </div>");
        sb.append("        <ul class=\"comment\">");
        sb.append("          <li class=\"comment__item\">");
        sb.append("            <div class=\"comment__item__user\">");
        sb.append("              <img class=\"comment__item__user__img\" />");
        sb.append("              <p class=\"comment__item__user__nickname\">account</p>");
        sb.append("            </div>");
        sb.append("            <p class=\"comment__item__article\">");
        sb.append("              군인 또는 군무원이 아닌 국민은 대한민국의 영역안에서는 중대한");
        sb.append("              군사상 기밀·초병·초소·유독음식물공급·포로·군용물에 관한 죄중");
        sb.append("              법률이 정한 경우와 비상계엄이 선포된 경우를 제외하고는 군사법원의");
        sb.append("              재판을 받지 아니한다.");
        sb.append("            </p>");
        sb.append("          </li>");
        sb.append("          <li class=\"comment__item\">");
        sb.append("            <div class=\"comment__item__user\">");
        sb.append("              <img class=\"comment__item__user__img\" />");
        sb.append("              <p class=\"comment__item__user__nickname\">account</p>");
        sb.append("            </div>");
        sb.append("            <p class=\"comment__item__article\">");
        sb.append("              대통령의 임기연장 또는 중임변경을 위한 헌법개정은 그 헌법개정 제안");
        sb.append("              당시의 대통령에 대하여는 효력이 없다. 민주평화통일자문회의의");
        sb.append("              조직·직무범위 기타 필요한 사항은 법률로 정한다.");
        sb.append("            </p>");
        sb.append("          </li>");
        sb.append("          <li class=\"comment__item\">");
        sb.append("            <div class=\"comment__item__user\">");
        sb.append("              <img class=\"comment__item__user__img\" />");
        sb.append("              <p class=\"comment__item__user__nickname\">account</p>");
        sb.append("            </div>");
        sb.append("            <p class=\"comment__item__article\">");
        sb.append("              민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로");
        sb.append("              정한다.");
        sb.append("            </p>");
        sb.append("          </li>");
        sb.append("          <li class=\"comment__item hidden\">");
        sb.append("            <div class=\"comment__item__user\">");
        sb.append("              <img class=\"comment__item__user__img\" />");
        sb.append("              <p class=\"comment__item__user__nickname\">account</p>");
        sb.append("            </div>");
        sb.append("            <p class=\"comment__item__article\">Comment 1</p>");
        sb.append("          </li>");
        sb.append("          <li class=\"comment__item hidden\">");
        sb.append("            <div class=\"comment__item__user\">");
        sb.append("              <img class=\"comment__item__user__img\" />");
        sb.append("              <p class=\"comment__item__user__nickname\">account</p>");
        sb.append("            </div>");
        sb.append("            <p class=\"comment__item__article\">Comment 2</p>");
        sb.append("          </li>");
        sb.append("          <li class=\"comment__item hidden\">");
        sb.append("            <div class=\"comment__item__user\">");
        sb.append("              <img class=\"comment__item__user__img\" />");
        sb.append("              <p class=\"comment__item__user__nickname\">account</p>");
        sb.append("            </div>");
        sb.append("            <p class=\"comment__item__article\">Comment 3</p>");
        sb.append("          </li>");
        sb.append("          <button id=\"show-all-btn\" class=\"btn btn_ghost btn_size_m\">");
        sb.append("            모든 댓글 보기(3개)");
        sb.append("          </button>");
        sb.append("        </ul>");
        sb.append("        <nav class=\"nav\">");
        sb.append("          <ul class=\"nav__menu\">");
        sb.append("            <li class=\"nav__menu__item\">");
        sb.append("              <a class=\"nav__menu__item__btn\" href=\"\">");
        sb.append("                <img");
        sb.append("                  class=\"nav__menu__item__img\"");
        sb.append("                  src=\"./img/ci_chevron-left.svg\"");
        sb.append("                />");
        sb.append("                이전 글");
        sb.append("              </a>");
        sb.append("            </li>");
        sb.append("            <li class=\"nav__menu__item\">");
        sb.append("              <a class=\"btn btn_ghost btn_size_m\">댓글 작성</a>");
        sb.append("            </li>");
        sb.append("            <li class=\"nav__menu__item\">");
        sb.append("              <a class=\"nav__menu__item__btn\" href=\"\">");
        sb.append("                다음 글");
        sb.append("                <img");
        sb.append("                  class=\"nav__menu__item__img\"");
        sb.append("                  src=\"./img/ci_chevron-right.svg\"");
        sb.append("                />");
        sb.append("              </a>");
        sb.append("            </li>");
        sb.append("          </ul>");
        sb.append("        </nav>");
        sb.append("      </div>");
        sb.append("    </div>");
        sb.append("  </body>");
        sb.append("</html>");

        return sb.toString();
    }
}
