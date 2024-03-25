package application.handler;

import webserver.HttpHandler.Handler;
import webserver.HttpHandler.Mapping.PostMapping;
import webserver.HttpMessage.*;
import webserver.HttpMessage.constants.eums.FileType;

import static webserver.HttpMessage.constants.WebServerConst.HTTP_VERSION;
import static webserver.HttpMessage.constants.eums.ResponseStatus.OK;

public class ArticleHandler implements Handler {
    private ResponseStartLine startLine;
    private MessageHeader responseHeader;
    private MessageBody responseBody;

    @PostMapping(path = "/article")
    public Response postArticle(Request request) {
        String title = request.getBody().getContentByKey("title");
        String article = request.getBody().getContentByKey("textfield");


        startLine = new ResponseStartLine(HTTP_VERSION , OK);
        responseBody = new MessageBody(title + "\n" + article + "posted !!!!" , FileType.TXT);
        responseHeader = writeContentResponseHeader(responseBody);

        return new Response(startLine).header(responseHeader).body(responseBody);
    }
}
