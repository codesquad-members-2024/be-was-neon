package application.handler.utils;

import application.db.Database;
import application.model.Article;
import application.model.User;
import webserver.HttpHandler.ResourceHandler;
import webserver.HttpMessage.Request;

public class HtmlMaker {


    public static String getArticlePage(Article article , String template){
        // template 에서 이미지소스 ,  글 내용 바꿈


        return "";
    }

    public static String getListHtml(){
        StringBuilder sb = new StringBuilder();
        sb.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>유저 목록</title>
                  회원가입 된 유저들
                </head>
                <body>
                """);

        for(User user : Database.findAll()){
            sb.append("<br>");
            sb.append(user.getName());
        }

        sb.append(
                "</body>\n" +
                "</html>");

        return sb.toString();
    }


}
