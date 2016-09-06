package ru.jleague13.images;

/**
 * @author ashevenkov 01.09.16 12:48.
 */
public class ImageInfo {

    public int id;
    public String name;
    public String contentType;

    public ImageInfo(int id, String name, String contentType) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
    }

    public String getPath() {
        return "/dimages/upload/" + id;
    }
}
