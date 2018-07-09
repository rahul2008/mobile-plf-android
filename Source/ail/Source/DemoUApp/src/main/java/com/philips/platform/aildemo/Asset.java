package com.philips.platform.aildemo;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.philips.platform.appinfra.contentloader.ContentInterface;

import java.util.List;

/**
 * Created by 310238114 on 12/13/2016.
 */

public class Asset implements ContentInterface {
    private String  assetType;
    private String  assetDescription;
    private String  assetURL;

    @Override
    public String getId() {
        return assetURL;
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
        return 1;
    }

    @Override
    public boolean isNewer(long version) {
        return false;
    }

    @Override
    public boolean parseInput(String json) {
        JsonElement response = new JsonParser().parse(json);
        this.assetURL= String.valueOf(response.getAsJsonObject().get("assetURL"));

        return true;
    }
}
