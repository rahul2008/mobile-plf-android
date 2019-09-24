package com.ecs.demotestuapp.jsonmodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Property implements Serializable {

    @SerializedName("tag")
    public String tag;

    @SerializedName("text")
    public String text;

    @SerializedName("hint")
    public String hint;

    @SerializedName("inputType")
    public int inputType = -1;
}
