package entity;

import java.util.List;

public class Article {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Article(String title, String date, String content, String url, String id) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.url = url;
        this.id = id;
    }

    public Article(String title, String date, String content, String url, String id, List<String> tags) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.url = url;
        this.id = id;
        this.tags = tags;
    }

    private String title;
    private String date;
    private String content;
    private String url;
    private String id;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    private List<String> tags;
}
