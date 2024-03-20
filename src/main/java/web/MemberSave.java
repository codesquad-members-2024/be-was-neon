package web;


import db.Database;
import http.HttpRequest;
import http.HttpRequest.HttpMethod;
import http.HttpResponse;
import model.User;

public class MemberSave extends HtmlProcessor {

    @Override
    public void process(HttpRequest request, HttpResponse response) {
        if (request.getMethod() == HttpMethod.POST) {
            Database.addUser(createUser(request));

            responseHeader302(response, getContentType(request), "/index.html");
            responseMessage(response, "ok");

            response.flush();
            return;
        }
        super.process(request, response);
    }

    private User createUser(HttpRequest request) {
        String id = request.getParameter("id");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return new User(id, password, username, email);
    }
}
