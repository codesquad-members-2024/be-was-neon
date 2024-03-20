package web;

import static utils.HttpConstant.SPLITTER;

import http.HttpRequest;
import http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import model.User;
import session.SessionManager;
import utils.HttpRequestParser;

public class DynamicHtmlProcessor extends HtmlProcessor {

    private final SessionManager sessionManager = new SessionManager();
    private final StringBuilder htmlBuilder = new StringBuilder();

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        Map<String, String> cookieMap = getCookieMap(request);
        String sessionId = getSessionId(cookieMap);
        Optional<Object> optionalSession = sessionManager.getSession(sessionId);

        if (optionalSession.isEmpty()) {
            // TODO: 로그인 버튼이 있는 화면 구현

            /* HTML 작성 */
            createHtmlHeader();
            createBodyTopPart();
            createLoginButton();
            createBodyCommentPart();

            /* http response 작성 */
            responseHeader200(response, getContentType(request));
            response.setContentLength(htmlBuilder.length());
            response.setMessageBody(htmlBuilder.toString());
            response.flush();

            htmlBuilder.setLength(0); // StringBuilder 초기화
            return;
        }

        // TODO: 사용자 이름이 있는 화면 구현
        User sessionUser = (User) optionalSession.get();
        String userName = sessionUser.getName();

        /* HTML 작성 */
        createHtmlHeader();
        createBodyTopPart();
        createUserProfile(userName);
        createBodyCommentPart();

        /* http response 작성 */
        responseHeader200(response, getContentType(request));
        response.setContentLength(htmlBuilder.length());
        response.setMessageBody(htmlBuilder.toString());
        response.flush();

        htmlBuilder.setLength(0); // StringBuilder 초기화
    }

    private String getSessionId(Map<String, String> cookieMap) {
        String sessionId = cookieMap.get("SID");
        if (sessionId == null) {
            return "";
        }
        return sessionId.split(SPLITTER)[0];
    }

    private Map<String, String> getCookieMap(HttpRequest request) {
        String cookie = request.getHeader("Cookie");
        return HttpRequestParser.parseParams(cookie);
    }

    private void createHtmlHeader() {
        // html header 생성
        htmlBuilder.append("<!DOCTYPE html>");
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<meta charset=\"UTF-8\" />");
        htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />");
        htmlBuilder.append("<link href=\"./reset.css\" rel=\"stylesheet\" />");
        htmlBuilder.append("<link href=\"./global.css\" rel=\"stylesheet\" />");
        htmlBuilder.append("<link href=\"./main.css\" rel=\"stylesheet\" />");
        htmlBuilder.append("</head>");
    }

    private void createBodyTopPart() {
        // html body 생성
        htmlBuilder.append("<body>");
        htmlBuilder.append("<div class=\"container\">");
        htmlBuilder.append("<header class=\"header\">");
        htmlBuilder.append("<a href=\"/\"><img src=\"./img/signiture.svg\" /></a>");
        htmlBuilder.append("<ul class=\"header__menu\">");
    }

    private void createLoginButton() {
        htmlBuilder.append("<li class=\"header__menu__item\" >");
        htmlBuilder.append("<a class=\"btn btn_contained btn_size_s\" href=\"/login\"> 로그인 </a>");
        htmlBuilder.append("</li >");

        htmlBuilder.append("<li class=\"header__menu__item\">");
        htmlBuilder.append("<a class=\"btn btn_ghost btn_size_s\" href=\"/registration\">회원 가입</a>");
        htmlBuilder.append("</li>");
    }

    private void createUserProfile(String userId) {
        htmlBuilder.append("<li class=\"header__menu__item\">");
        htmlBuilder.append("<div class=\"comment__item__user\">");
        htmlBuilder.append("<img class=\"comment__item__user__img\" />");
        htmlBuilder.append("<p class=\"comment__item__user__nickname\" style=\"padding: 15px\">" + userId + "</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</li>");

        htmlBuilder.append("<li class=\"header__menu__item\">");
        htmlBuilder.append("<a class=\"btn btn_contained btn_size_s\" href=\"/article\">글쓰기</a>");
        htmlBuilder.append("</li>");

        htmlBuilder.append("<li class=\"header__menu__item\">");
        htmlBuilder.append("<button id=\"logout-btn\" class=\"btn btn_ghost btn_size_s\">로그아웃</button>");
        htmlBuilder.append("</li>");

        htmlBuilder.append("</ul>");
        htmlBuilder.append("</header>");
    }

    private void createBodyCommentPart() {
        // body header 닫음
        htmlBuilder.append("</ul>");
        htmlBuilder.append("</header>");

        // 게시물과 댓글 섹션 추가
        htmlBuilder.append("<div class=\"wrapper\">");
        htmlBuilder.append("<div class=\"post\">");
        htmlBuilder.append("<div class=\"post__account\">");
        htmlBuilder.append("<img class=\"post__account__img\" />");
        htmlBuilder.append("<p class=\"post__account__nickname\">account</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<img class=\"post__img\" />");
        htmlBuilder.append("<div class=\"post__menu\">");
        htmlBuilder.append("<ul class=\"post__menu__personal\">");
        htmlBuilder.append("<li>");
        htmlBuilder.append("<button class=\"post__menu__btn\">");
        htmlBuilder.append("<img src=\"./img/like.svg\" />");
        htmlBuilder.append("</button>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li>");
        htmlBuilder.append("<button class=\"post__menu__btn\">");
        htmlBuilder.append("<img src=\"./img/sendLink.svg\" />");
        htmlBuilder.append("</button>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("</ul>");
        htmlBuilder.append("<button class=\"post__menu__btn\">");
        htmlBuilder.append("<img src=\"./img/bookMark.svg\" />");
        htmlBuilder.append("</button>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p class=\"post__article\">");
        htmlBuilder.append(
                "우리는 시스템 아키텍처에 대한 일관성 있는 접근이 필요하며, 필요한 모든 측면은 이미 개별적으로 인식되고 있다고 생각합니다. 즉, 응답이 잘 되고, 탄력적이며 유연하고 메시지 기반으로 동작하는 시스템 입니다. 우리는 이것을 리액티브 시스템(Reactive Systems)라고 부릅니다. 리액티브 시스템으로 구축된 시스템은 보다 유연하고, 느슨한 결합을 갖고, 확장성 이 있습니다. 이로 인해 개발이 더 쉬워지고 변경 사항을 적용하기 쉬워집니다. 이 시스템은 장애 에 대해 더 강한 내성을 지니며, 비록 장애가 발생 하더라도, 재난이 일어나기 보다는 간결한 방식으로 해결합니다. 리액티브 시스템은 높은 응답성을 가지며 사용자 에게 효과적인 상호적 피드백을 제공합니다.");
        htmlBuilder.append("</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<ul class=\"comment\">");
        htmlBuilder.append("<li class=\"comment__item\">");
        htmlBuilder.append("<div class=\"comment__item__user\">");
        htmlBuilder.append("<img class=\"comment__item__user__img\" />");
        htmlBuilder.append("<p class=\"comment__item__user__nickname\">account</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p class=\"comment__item__article\">");
        htmlBuilder.append("군인 또는 군무원이 아닌 국민은 대한민국의 영역안에서는 중대한 군사상 기밀·초병·초소·유독음식물공급·포로·군용물에 관한 죄중 법률이 정한 경우와 비상계엄이 선포된 경우를 제외하고는 군사법원의 재판을 받지 아니한다.");
        htmlBuilder.append("</p>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li class=\"comment__item\">");
        htmlBuilder.append("<div class=\"comment__item__user\">");
        htmlBuilder.append("<img class=\"comment__item__user__img\" />");
        htmlBuilder.append("<p class=\"comment__item__user__nickname\">account</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p class=\"comment__item__article\">");
        htmlBuilder.append("대통령의 임기연장 또는 중임변경을 위한 헌법개정은 그 헌법개정 제안 당시의 대통령에 대하여는 효력이 없다. 민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다.");
        htmlBuilder.append("</p>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li class=\"comment__item\">");
        htmlBuilder.append("<div class=\"comment__item__user\">");
        htmlBuilder.append("<img class=\"comment__item__user__img\" />");
        htmlBuilder.append("<p class=\"comment__item__user__nickname\">account</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p class=\"comment__item__article\">");
        htmlBuilder.append("민주평화통일자문회의의 조직·직무범위 기타 필요한 사항은 법률로 정한다.");
        htmlBuilder.append("</p>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li class=\"comment__item hidden\">");
        htmlBuilder.append("<div class=\"comment__item__user\">");
        htmlBuilder.append("<img class=\"comment__item__user__img\" />");
        htmlBuilder.append("<p class=\"comment__item__user__nickname\">account</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p class=\"comment__item__article\">Comment 1</p>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li class=\"comment__item hidden\">");
        htmlBuilder.append("<div class=\"comment__item__user\">");
        htmlBuilder.append("<img class=\"comment__item__user__img\" />");
        htmlBuilder.append("<p class=\"comment__item__user__nickname\">account</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p class=\"comment__item__article\">Comment 2</p>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li class=\"comment__item hidden\">");
        htmlBuilder.append("<div class=\"comment__item__user\">");
        htmlBuilder.append("<img class=\"comment__item__user__img\" />");
        htmlBuilder.append("<p class=\"comment__item__user__nickname\">account</p>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("<p class=\"comment__item__article\">Comment 3</p>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<button id=\"show-all-btn\" class=\"btn btn_ghost btn_size_m\">");
        htmlBuilder.append("모든 댓글 보기(3개)");
        htmlBuilder.append("</button>");
        htmlBuilder.append("</ul>");
        htmlBuilder.append("<nav class=\"nav\">");
        htmlBuilder.append("<ul class=\"nav__menu\">");
        htmlBuilder.append("<li class=\"nav__menu__item\">");
        htmlBuilder.append("<a class=\"nav__menu__item__btn\" href=\"\">");
        htmlBuilder.append("<img class=\"nav__menu__item__img\" src=\"./img/ci_chevron-left.svg\" />");
        htmlBuilder.append("이전 글");
        htmlBuilder.append("</a>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li class=\"nav__menu__item\">");
        htmlBuilder.append("<a class=\"btn btn_ghost btn_size_m\" href=\"/comment\">댓글 작성</a>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("<li class=\"nav__menu__item\">");
        htmlBuilder.append("<a class=\"nav__menu__item__btn\" href=\"\">");
        htmlBuilder.append("다음 글");
        htmlBuilder.append("<img class=\"nav__menu__item__img\" src=\"./img/ci_chevron-right.svg\" />");
        htmlBuilder.append("</a>");
        htmlBuilder.append("</li>");
        htmlBuilder.append("</ul>");
        htmlBuilder.append("</nav>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");
    }
}
