/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 310238114 on 6/7/2016.
 */
public class Config {
    private String micrositeId;
    private HashMap<String , String > urls;
    private ArrayList<Tag> tags;


    public String getMicrositeId() {
        return micrositeId;
    }

    public void setMicrositeId(String micrositeId) {
        this.micrositeId = micrositeId;
    }

    public HashMap<String, String> getUrls() {
        return urls;
    }

    public void setUrls(HashMap<String, String> urls) {
        this.urls = urls;
    }


    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }


}
