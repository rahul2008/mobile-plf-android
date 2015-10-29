package com.philips.cdp.serviceapi.productinformation.features;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FeatureHighlight {

@SerializedName("featureCode")
@Expose
private String featureCode;
@SerializedName("featureReferenceName")
@Expose
private String featureReferenceName;
@SerializedName("featureHighlightRank")
@Expose
private String featureHighlightRank;

/**
* No args constructor for use in serialization
* 
*/
public FeatureHighlight() {
}

/**
* 
* @param featureHighlightRank
* @param featureCode
* @param featureReferenceName
*/
public FeatureHighlight(String featureCode, String featureReferenceName, String featureHighlightRank) {
this.featureCode = featureCode;
this.featureReferenceName = featureReferenceName;
this.featureHighlightRank = featureHighlightRank;
}

/**
* 
* @return
* The featureCode
*/
public String getFeatureCode() {
return featureCode;
}

/**
* 
* @param featureCode
* The featureCode
*/
public void setFeatureCode(String featureCode) {
this.featureCode = featureCode;
}

public FeatureHighlight withFeatureCode(String featureCode) {
this.featureCode = featureCode;
return this;
}

/**
* 
* @return
* The featureReferenceName
*/
public String getFeatureReferenceName() {
return featureReferenceName;
}

/**
* 
* @param featureReferenceName
* The featureReferenceName
*/
public void setFeatureReferenceName(String featureReferenceName) {
this.featureReferenceName = featureReferenceName;
}

public FeatureHighlight withFeatureReferenceName(String featureReferenceName) {
this.featureReferenceName = featureReferenceName;
return this;
}

/**
* 
* @return
* The featureHighlightRank
*/
public String getFeatureHighlightRank() {
return featureHighlightRank;
}

/**
* 
* @param featureHighlightRank
* The featureHighlightRank
*/
public void setFeatureHighlightRank(String featureHighlightRank) {
this.featureHighlightRank = featureHighlightRank;
}

public FeatureHighlight withFeatureHighlightRank(String featureHighlightRank) {
this.featureHighlightRank = featureHighlightRank;
return this;
}

}

