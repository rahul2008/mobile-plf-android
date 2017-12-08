package com.philips.platform.catalogapp.dataUtils;

import java.io.Serializable;

public class GridData implements Serializable {

    private int thumbnail;
    private String title, description;
    private boolean isFavorite;

    public GridData(int thumbnail, String title, String description, boolean isFavorite) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.isFavorite = isFavorite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite(){
        return isFavorite;
    }

    public void toggleFavorite(){
        isFavorite = !isFavorite;
    }
}
