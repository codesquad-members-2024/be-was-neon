package webserver.requesthandler;

public interface URLConst {
    String HOME_URL = "/";
    String LOGGED_IN_USER_HOME_URL = "/main";
    String REGISTRATION_URL = "/registration";
    String LOGIN_URL = "/login";
    String LOGOUT_URL = "/logout";
    String ARTICLE_URL = "/article";
    String COMMENT_URL = "/comment";
    String USER_LIST_URL = "/user/list";
    String STATIC_DIRECTORY = "/static";
    String INDEX_HTML_FILE = "/index.html";
    String IMG_FILE_URL = "/img/.*";
    String CSS_FILE_URL = "/css/.*";
}
