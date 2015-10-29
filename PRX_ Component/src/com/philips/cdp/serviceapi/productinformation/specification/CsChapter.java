package com.philips.cdp.serviceapi.productinformation.specification;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CsChapter {

@SerializedName("csChapterCode")
@Expose
private String csChapterCode;
@SerializedName("csChapterName")
@Expose
private String csChapterName;
@SerializedName("csChapterRank")
@Expose
private String csChapterRank;
@SerializedName("csItem")
@Expose
private List<CsItem> csItem = new ArrayList<CsItem>();

/**
* No args constructor for use in serialization
* 
*/
public CsChapter() {
}

/**
* 
* @param csChapterName
* @param csChapterCode
* @param csChapterRank
* @param csItem
*/
public CsChapter(String csChapterCode, String csChapterName, String csChapterRank, List<CsItem> csItem) {
this.csChapterCode = csChapterCode;
this.csChapterName = csChapterName;
this.csChapterRank = csChapterRank;
this.csItem = csItem;
}

/**
* 
* @return
* The csChapterCode
*/
public String getCsChapterCode() {
return csChapterCode;
}

/**
* 
* @param csChapterCode
* The csChapterCode
*/
public void setCsChapterCode(String csChapterCode) {
this.csChapterCode = csChapterCode;
}

public CsChapter withCsChapterCode(String csChapterCode) {
this.csChapterCode = csChapterCode;
return this;
}

/**
* 
* @return
* The csChapterName
*/
public String getCsChapterName() {
return csChapterName;
}

/**
* 
* @param csChapterName
* The csChapterName
*/
public void setCsChapterName(String csChapterName) {
this.csChapterName = csChapterName;
}

public CsChapter withCsChapterName(String csChapterName) {
this.csChapterName = csChapterName;
return this;
}

/**
* 
* @return
* The csChapterRank
*/
public String getCsChapterRank() {
return csChapterRank;
}

/**
* 
* @param csChapterRank
* The csChapterRank
*/
public void setCsChapterRank(String csChapterRank) {
this.csChapterRank = csChapterRank;
}

public CsChapter withCsChapterRank(String csChapterRank) {
this.csChapterRank = csChapterRank;
return this;
}

/**
* 
* @return
* The csItem
*/
public List<CsItem> getCsItem() {
return csItem;
}

/**
* 
* @param csItem
* The csItem
*/
public void setCsItem(List<CsItem> csItem) {
this.csItem = csItem;
}

public CsChapter withCsItem(List<CsItem> csItem) {
this.csItem = csItem;
return this;
}

}

