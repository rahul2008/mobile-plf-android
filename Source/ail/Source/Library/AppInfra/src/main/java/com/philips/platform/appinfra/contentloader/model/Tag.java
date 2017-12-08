package com.philips.platform.appinfra.contentloader.model;

/**
 * The Content loader Tag model class.
 */

public class Tag {

    private Boolean isVisibleOnWeb;
    private String name;
    private String key;
    private String id;

    public Boolean getVisibleOnWeb() {
        return isVisibleOnWeb;
    }

    public void setVisibleOnWeb(Boolean visibleOnWeb) {
        isVisibleOnWeb = visibleOnWeb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

}
