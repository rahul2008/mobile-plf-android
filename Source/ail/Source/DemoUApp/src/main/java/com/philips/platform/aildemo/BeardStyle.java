package com.philips.platform.aildemo;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.philips.platform.appinfra.contentloader.ContentInterface;

import java.util.List;

/**
 * Created by 310238114 on 12/13/2016.
 */

public class BeardStyle implements ContentInterface {

    private String uid;
    private String icon;
    private String title;


    @Override
    public String getId() {
        return uid;
    }

    @Override
    public boolean hasTag(String tagID) {
        return false;
    }

    @Override
    public List<String> getTags() {
        return null;
    }

    @Override
    public long getVersion() {
        return 0;
    }

    @Override
    public boolean isNewer(long version) {
        return false;
    }

    @Override
    public boolean parseInput(String json) {
        JsonElement response = new JsonParser().parse(json);
        this.uid = String.valueOf(response.getAsJsonObject().get("uid"));
        this.icon = String.valueOf(response.getAsJsonObject().get("icon"));
        this.title = String.valueOf(response.getAsJsonObject().get("title"));
        return true;
    }
}
