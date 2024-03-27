package application.model;

public class Article {
    private String encodedImg;
    private final String content;
    private final String writer;

    public Article(String content , String writer){
        this.content = content;
        this.writer = writer;
    }

    public Article(String content , String encodedImg, String writer){
        this.encodedImg = encodedImg;
        this.content = content;
        this.writer = writer;
    }

    public String getEncodedImg() {
        return encodedImg;
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }
}
