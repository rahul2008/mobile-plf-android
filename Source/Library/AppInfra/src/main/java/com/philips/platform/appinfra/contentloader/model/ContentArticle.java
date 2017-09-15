/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.contentloader.model;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.philips.platform.appinfra.contentloader.ContentInterface;

import java.util.ArrayList;
import java.util.List;

/*
 * The Content loader Content Article model class.
 * Article example: https://www.philips.com/wrx/b2c/c/nl/nl/ugrow-app/home.api.v1.offset.(0).limit.(100).json
 */
public class ContentArticle implements ContentInterface {

    private String overlay;
    private String linkurl;
    private String text;
    private String description;
    private String title;
    // private URL previewimage;
    // private URL portraitimage;
    private String imageUrl;
    private String articleDescription;
    private List<Tag> tags = new ArrayList<>();
    private List<String> tagIds = new ArrayList<>();
    //private List<Map<String,String>> tags;
    private String link;
    // private ASSET[] assets;
    private long modDate;
    private String uid;


    // region ContentInterface implementation
    @Override
    public String getId() {
        return uid;
    }

    @Override
    public boolean hasTag(String tag) {
        return tagIds != null && tagIds.contains(tag);
    }

    @Override
    public List<String> getTags() {
        return tagIds;
    }

//    @Override
//    public List<String> getTag() {
//        return tagList;
//    }

    public List<Tag> getTagsList() {
        return tags;
    }


    @Override
    public long getVersion() {
        return modDate;
    }

    @Override
    public boolean isNewer(long version) {
        return (modDate > version);
    }

    @Override
    public boolean parseInput(String json) {
        final JsonElement response = new JsonParser().parse(json);
        this.title = String.valueOf(response.getAsJsonObject().get("title"));
        this.link = String.valueOf(response.getAsJsonObject().get("link"));
        this.linkurl = String.valueOf(response.getAsJsonObject().get("link"));
        this.imageUrl = String.valueOf(response.getAsJsonObject().get("imageUrl"));
        this.text = String.valueOf(response.getAsJsonObject().get("text"));
        this.overlay = String.valueOf(response.getAsJsonObject().get("overlay"));
        this.articleDescription = String.valueOf(response.getAsJsonObject().get("description"));
        this.uid = String.valueOf(response.getAsJsonObject().get("uid"));
        final JsonElement tagElement = response.getAsJsonObject().get("tags");

        if (tagElement != null && tagElement.isJsonArray()) {
            final JsonArray tagArray = tagElement.getAsJsonArray();
            if (tagArray != null) {
                for (int i = 0; i < tagArray.size(); i++) {
                    JsonElement tagobj = tagArray.get(i);
                    Tag tag = new Tag();
                    String tagName = String.valueOf(tagobj.getAsJsonObject().get("name"));
                    String tagId = String.valueOf(tagobj.getAsJsonObject().get("id"));
                    String key = String.valueOf(tagobj.getAsJsonObject().get("key"));
                    tag.setId(tagId);
                    tag.setName(tagName);
                    tag.setKey(key);
                    tags.add(tag);
                    tagIds.add(tagId);
                }
            }
        }
        return true;
    }

    public String getOverlay() {
        return overlay;
    }

    public void setOverlay(String overlay) {
        this.overlay = overlay;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getModDate() {
        return modDate;
    }

    public void setModDate(long modDate) {
        this.modDate = modDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<String> tagIds) {
        this.tagIds = tagIds;
    }
}
