package com.philips.cdp.di.ecs.prx.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Item model class.
 */

public class Item {

@SerializedName("head")
@Expose
private String head;
@SerializedName("code")
@Expose
private String code;
@SerializedName("rank")
@Expose
private String rank;
@SerializedName("lang")
@Expose
private String lang;
@SerializedName("asset")
@Expose
private String asset;

/**
*
* @return
* The head
*/
public String getHead() {
return head;
}

/**
*
* @param head
* The head
*/
public void setHead(String head) {
this.head = head;
}

/**
*
* @return
* The code
*/
public String getCode() {
return code;
}

/**
*
* @param code
* The code
*/
public void setCode(String code) {
this.code = code;
}

/**
*
* @return
* The rank
*/
public String getRank() {
return rank;
}

/**
*
* @param rank
* The rank
*/
public void setRank(String rank) {
this.rank = rank;
}

/**
*
* @return
* The lang
*/
public String getLang() {
return lang;
}

/**
*
* @param lang
* The lang
*/
public void setLang(String lang) {
this.lang = lang;
}

/**
*
* @return
* The asset
*/
public String getAsset() {
return asset;
}

/**
*
* @param asset
* The asset
*/
public void setAsset(String asset) {
this.asset = asset;
}

}
