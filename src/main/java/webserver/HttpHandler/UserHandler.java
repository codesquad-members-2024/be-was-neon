package webserver.HttpHandler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMessage.*;
import webserver.Mapping.GetMapping;
import webserver.Mapping.PostMapping;
import webserver.eums.FileType;

import java.util.HashMap;

import static webserver.eums.ResponseStatus.FOUND;
import static webserver.eums.ResponseStatus.OK;

public class UserHandler implements Handler{

    private ResponseStartLine startLine;
    private MessageHeader responseHeader = new MessageHeader(new HashMap<>());
    private MessageBody responseBody;

    private static final Logger log = LoggerFactory.getLogger(ResourceHandler.class);

    @PostMapping(path = "/create")
    public Response createUser(Request request) {
        MessageBody reqBody = request.getBody();

        try {
            User user = new User(
                    reqBody.getContentByKey("userId"),
                    reqBody.getContentByKey("password"),
                    reqBody.getContentByKey("name"),
                    reqBody.getContentByKey("email")
            );

            Database.addUser(user);
            log.info("User Created : " + user.getUserId());
        } catch (IllegalArgumentException fail) {
            log.info("Fail to create new user : " + fail.getMessage());
        }

        startLine = new ResponseStartLine("HTTP/1.1", FOUND);
        responseHeader.addHeaderField("Location", "/");

        return new Response(startLine).header(responseHeader);
    }

    @GetMapping(path = "/user/list")
    public Response userList(Request request) {
        if (!verifySession(request)) {
            startLine = new ResponseStartLine("HTTP/1.1", FOUND);
            responseHeader.addHeaderField("Location", "/");

            return new Response(startLine).header(responseHeader);
        }

        startLine = new ResponseStartLine("HTTP/1.1", OK);
        responseBody = new MessageBody(HtmlMaker.getListHtml(), FileType.HTML);
        responseHeader = writeContentResponseHeader(responseBody);

        return new Response(startLine).header(responseHeader).body(responseBody);
    }
}
