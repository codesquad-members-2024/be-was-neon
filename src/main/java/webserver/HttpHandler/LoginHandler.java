package webserver.HttpHandler;

import db.Database;
import db.SessionStore;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMessage.*;
import webserver.Mapping.PostMapping;

import static webserver.eums.ResponseStatus.FOUND;

public class LoginHandler implements Handler{

    private ResponseStartLine startLine;
    private MessageHeader responseHeader;
    private MessageBody responseBody;

    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);

    @PostMapping(path = "/login")
    public Response login(Request request) {
        MessageBody requestBody = request.getBody();
        User user = Database.findUserById(requestBody.getContentByKey("userId"));

        responseHeader = MessageHeader.builder().field("Location" , "/").build();

        try {
            if (user.getPassword().equals(requestBody.getContentByKey("password"))) {
                String cookie = responseHeader.addCookie(10);
                SessionStore.addSession(cookie, user);
                log.info("login : " + user.getName());
            } else {
                log.info("login failed : password mismatch");
            }
        } catch (NullPointerException notExistUser) {
            log.info("login failed : notExistUser");
        }

        startLine = new ResponseStartLine("HTTP/1.1", FOUND);
        return new Response(startLine).header(responseHeader);
    }

    @PostMapping(path = "/logout")
    public Response logout(Request request) {
        String cookie = request.getHeaderValue("Cookie").split("sid=")[1];
        SessionStore.removeSession(cookie);
        log.info("logout");

        startLine = new ResponseStartLine("HTTP/1.1", FOUND);
        responseHeader = MessageHeader.builder().field("Location" , "/").build();
        return new Response(startLine).header(responseHeader);
    }
}
