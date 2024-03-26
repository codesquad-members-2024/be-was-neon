package application.model;

public class Article {
    private String imagePath;
    private final String content;
    private final User writer;

    public Article(String content , User writer){
        this.content = content;
        this.writer = writer;
    }

    public Article(String content , String imagePath, User writer){
        this.imagePath = imagePath;
        this.content = content;
        this.writer = writer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getContent() {
        return content;
    }

    public User getWriter() {
        return writer;
    }
}
