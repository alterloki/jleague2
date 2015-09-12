package ru.jleague13.entity;

/**
 * @author ashevenkov 06.09.15 21:48.
 */
public class NewsItem {

    private int id;
    private String title;
    private String text;

    public NewsItem() {
    }

    public NewsItem(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
