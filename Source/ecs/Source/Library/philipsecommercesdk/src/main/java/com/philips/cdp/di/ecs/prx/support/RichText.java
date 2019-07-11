package com.philips.cdp.di.ecs.prx.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Rich Text Class.
 */

public class RichText {

@SerializedName("type")
@Expose
private String type;
@SerializedName("chapter")
@Expose
private Chapter chapter;
@SerializedName("item")
@Expose
private List<Item> item = new ArrayList<Item>();

/**
*
* @return
* The type
*/
public String getType() {
return type;
}

/**
*
* @param type
* The type
*/
public void setType(String type) {
this.type = type;
}

/**
*
* @return
* The chapter
*/
public Chapter getChapter() {
return chapter;
}

/**
*
* @param chapter
* The chapter
*/
public void setChapter(Chapter chapter) {
this.chapter = chapter;
}

/**
*
* @return
* The item
*/
public List<Item> getItem() {
return item;
}

/**
*
* @param item
* The item
*/
public void setItem(List<Item> item) {
this.item = item;
}

}
