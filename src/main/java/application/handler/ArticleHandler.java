package application.handler;

import application.handler.utils.HtmlMaker;
import application.model.Article;
import application.model.User;
import webserver.HttpHandler.Handler;
import webserver.HttpHandler.Mapping.*;
import webserver.HttpHandler.ResourceHandler;
import webserver.HttpMessage.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static webserver.HttpMessage.constants.WebServerConst.*;
import static webserver.HttpMessage.constants.eums.FileType.HTML;
import static webserver.HttpMessage.constants.eums.FileType.PNG;
import static webserver.HttpMessage.constants.eums.ResponseStatus.*;
import static webserver.WebServer.staticSourcePath;

public class ArticleHandler implements Handler {
    private ResponseStartLine startLine;
    private MessageHeader responseHeader;
    private MessageBody responseBody;


    private static final ResourceHandler resourceHandler = new ResourceHandler();
    private final List<Article> articleList = new ArrayList<>();

    @PostMapping(path = "/article")
    public Response postArticle(Request request) {
        startLine = new ResponseStartLine(HTTP_VERSION, FOUND);

        try {
            createArticle(request);
            responseHeader = MessageHeader.builder().field(LOCATION, "/main/article?index=1").build();
        }catch (IOException e){
            responseHeader = MessageHeader.builder().field(LOCATION, "/").build();
        }

        return new Response(startLine).header(responseHeader).body(responseBody);
    }

    private void createArticle(Request request) throws IOException {
        User writer = getCookie(request);
        String content = request.getBody().getContentByKey("textfield");
        String imagePath = staticSourcePath + "/img/post/" + (articleList.size() + 1);
        String image;

        if ((image = request.getBody().getContentByKey("image")) != null) {
            FileOutputStream fi = new FileOutputStream(imagePath);
            fi.write(image.getBytes());
            fi.close();

            articleList.add(new Article(content, imagePath , writer));
        } else articleList.add(new Article(content , writer));
    }

    @GetMapping(path = "/main/article")
    public Response getArticle(Request request) {
        int index = Integer.parseInt(request.getRequestQuery("index")) - 1;

        startLine = new ResponseStartLine(HTTP_VERSION, OK);
        responseBody = new MessageBody(HtmlMaker.getArticlePage(
                articleList.get(index), new String(resourceHandler.responseGet(request).getBody())), HTML);

        writeContentResponseHeader(responseBody);

        return new Response(startLine).header(responseHeader).body(responseBody);
    }

    @GetMapping(path = "test/img")
    public Response getTestImg(Request request){
        startLine = new ResponseStartLine(HTTP_VERSION, OK);
        responseBody = new MessageBody(" " , PNG);
        writeContentResponseHeader(responseBody);
        return new Response(startLine).header(responseHeader).body(responseBody);
    }
}
