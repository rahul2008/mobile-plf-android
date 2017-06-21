package com.philips.platform.appframework.testmicroappfw.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public class Chapter implements Serializable{
    @SerializedName("chapterName")
    private String chapterName;

    @SerializedName("commonComponents")
    private ArrayList<CommonComponent> commonComponentsList;

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public ArrayList<CommonComponent> getCommonComponentsList() {
        return commonComponentsList;
    }

    public void setCommonComponentsList(ArrayList<CommonComponent> commonComponentsList) {
        this.commonComponentsList = commonComponentsList;
    }
}
