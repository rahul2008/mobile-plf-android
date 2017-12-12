/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.models;

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
