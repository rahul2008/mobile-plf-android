package com.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class KeyLogo {

@SerializedName("title")
@Expose
private String title;
@SerializedName("imageURL")
@Expose
private String imageURL;

/**
* No args constructor for use in serialization
* 
*/
public KeyLogo() {
}

/**
* 
* @param title
* @param imageURL
*/
public KeyLogo(String title, String imageURL) {
this.title = title;
this.imageURL = imageURL;
}

/**
* 
* @return
* The title
*/
public String getTitle() {
return title;
}

/**
* 
* @param title
* The title
*/
public void setTitle(String title) {
this.title = title;
}

public KeyLogo withTitle(String title) {
this.title = title;
return this;
}

/**
* 
* @return
* The imageURL
*/
public String getImageURL() {
return imageURL;
}

/**
* 
* @param imageURL
* The imageURL
*/
public void setImageURL(String imageURL) {
this.imageURL = imageURL;
}

public KeyLogo withImageURL(String imageURL) {
this.imageURL = imageURL;
return this;
}

}
