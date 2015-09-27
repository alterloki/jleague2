package ru.jleague13.entity;

import java.util.Date;

/**
 * @author ashevenkov 24.09.15 20:48.
 */
public class NewsArticle {

    private int id;
    private String title;
    private Date date;
    private String fullText;
    private User author;

    public NewsArticle(int id, String title, String text, Date date, User author) {
        this.id = id;
        this.title = title;
        this.fullText = text;
        this.author = author;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
