package webserver.handler;

import db.Database;
import model.User;
import webserver.DynamicHtml;
import webserver.Session.SessionManger;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.util.Collection;

import static webserver.handler.Path.HOME_PAGE;

public class UserListHandler implements Handler {

    @Override
    public HttpResponse service(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        Collection<User> users = Database.findAll();

        String sid = request.getSessionId();
        if (!SessionManger.isLogin(sid)) {
            response.redirect(HOME_PAGE.getPath());
        }

        response.setStatus(HttpStatus.OK);
        response.setBody(DynamicHtml.getUserListHtml(users));

        return response;
    }


}
