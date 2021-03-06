package ru.jleague13.entity;

import com.google.common.base.Objects;

/**
 * @author ashevenkov 13.09.15 19:23.
 */
public class Country {

    private int id;
    private String faId;
    private String faIndex;
    private String name;
    private String picture;

    public Country() {
    }

    public Country(int id, String faId, String name, String faIndex) {
        this.id = id;
        this.faId = faId;
        this.name = name;
        this.faIndex = faIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFaId() {
        return faId;
    }

    public void setFaId(String faId) {
        this.faId = faId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaIndex() {
        return faIndex;
    }

    public void setFaIndex(String faIndex) {
        this.faIndex = faIndex;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equal(faIndex, country.faIndex) &&
                Objects.equal(name, country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(faId, faIndex, name);
    }
}
