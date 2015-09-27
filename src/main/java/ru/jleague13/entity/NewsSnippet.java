package ru.jleague13.entity;

import java.util.Date;

/**
 * @author ashevenkov 06.09.15 21:48.
 */
public class NewsSnippet {

    private int id;
    private String title;
    private String shortText;
    private User author;
    private Date postDate;
    private String picture;

    public NewsSnippet() {
    }

    public NewsSnippet(int id, String title, String shortText, Date date, User author) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.author = author;
        this.postDate = date;
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

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
