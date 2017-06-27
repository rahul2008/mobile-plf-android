package com.philips.platform.appframework.testmicroappfw.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by philips on 13/02/17.
 */

public class TestFwConfig {
    @SerializedName("chapters")
    private ArrayList<Chapter> chaptersList;

    public ArrayList<Chapter> getChaptersList() {
        return chaptersList;
    }

    public void setChaptersList(ArrayList<Chapter> chaptersList) {
        this.chaptersList = chaptersList;
    }
}
