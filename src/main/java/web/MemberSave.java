package web;

import db.Database;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class MemberSave extends HtmlProcessor {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        if (request.getParameter("id").isEmpty()) {
            super.process(request, response);
            return;
        }
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Database.addUser(new User(id, password, username, email));
        super.process(request, response);
    }
}
