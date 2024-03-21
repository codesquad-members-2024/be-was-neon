package webserver.handler;

import model.User;
import webserver.html.DynamicHtml;
import webserver.Session.SessionManger;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.io.File;

import static webserver.handler.Path.HOME_PAGE;


public class HomeHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        HttpResponse response = new HttpResponse();

        String sid = request.getSessionId();
        if (SessionManger.isLogin(sid)) {
            User user = SessionManger.getUser(sid);
            response.setStatus(HttpStatus.OK);
            response.setBody(DynamicHtml.getLoginIndexHtml(user));
            return response;
        }

        File file = new File(HOME_PAGE.getRelativePath());
        response.setStatus(HttpStatus.OK);
        response.setBody(file);
        return response;
    }
}
