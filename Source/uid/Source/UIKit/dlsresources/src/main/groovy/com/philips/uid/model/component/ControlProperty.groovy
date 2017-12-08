package com.philips.uid.model.component;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ControlProperty {

    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("value")
    @Expose
    private String value;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}