package com.philips.cdp.serviceapi.productinformation.features;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Feature {

@SerializedName("featureCode")
@Expose
private String featureCode;
@SerializedName("featureReferenceName")
@Expose
private String featureReferenceName;
@SerializedName("featureName")
@Expose
private String featureName;
@SerializedName("featureLongDescription")
@Expose
private String featureLongDescription;
@SerializedName("featureGlossary")
@Expose
private String featureGlossary;
@SerializedName("featureRank")
@Expose
private String featureRank;
@SerializedName("featureTopRank")
@Expose
private String featureTopRank;

/**
* No args constructor for use in serialization
* 
*/
public Feature() {
}

/**
* 
* @param featureRank
* @param featureName
* @param featureCode
* @param featureGlossary
* @param featureReferenceName
* @param featureTopRank
* @param featureLongDescription
*/
public Feature(String featureCode, String featureReferenceName, String featureName, String featureLongDescription, String featureGlossary, String featureRank, String featureTopRank) {
this.featureCode = featureCode;
this.featureReferenceName = featureReferenceName;
this.featureName = featureName;
this.featureLongDescription = featureLongDescription;
this.featureGlossary = featureGlossary;
this.featureRank = featureRank;
this.featureTopRank = featureTopRank;
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

public Feature withFeatureCode(String featureCode) {
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

public Feature withFeatureReferenceName(String featureReferenceName) {
this.featureReferenceName = featureReferenceName;
return this;
}

/**
* 
* @return
* The featureName
*/
public String getFeatureName() {
return featureName;
}

/**
* 
* @param featureName
* The featureName
*/
public void setFeatureName(String featureName) {
this.featureName = featureName;
}

public Feature withFeatureName(String featureName) {
this.featureName = featureName;
return this;
}

/**
* 
* @return
* The featureLongDescription
*/
public String getFeatureLongDescription() {
return featureLongDescription;
}

/**
* 
* @param featureLongDescription
* The featureLongDescription
*/
public void setFeatureLongDescription(String featureLongDescription) {
this.featureLongDescription = featureLongDescription;
}

public Feature withFeatureLongDescription(String featureLongDescription) {
this.featureLongDescription = featureLongDescription;
return this;
}

/**
* 
* @return
* The featureGlossary
*/
public String getFeatureGlossary() {
return featureGlossary;
}

/**
* 
* @param featureGlossary
* The featureGlossary
*/
public void setFeatureGlossary(String featureGlossary) {
this.featureGlossary = featureGlossary;
}

public Feature withFeatureGlossary(String featureGlossary) {
this.featureGlossary = featureGlossary;
return this;
}

/**
* 
* @return
* The featureRank
*/
public String getFeatureRank() {
return featureRank;
}

/**
* 
* @param featureRank
* The featureRank
*/
public void setFeatureRank(String featureRank) {
this.featureRank = featureRank;
}

public Feature withFeatureRank(String featureRank) {
this.featureRank = featureRank;
return this;
}

/**
* 
* @return
* The featureTopRank
*/
public String getFeatureTopRank() {
return featureTopRank;
}

/**
* 
* @param featureTopRank
* The featureTopRank
*/
public void setFeatureTopRank(String featureTopRank) {
this.featureTopRank = featureTopRank;
}

public Feature withFeatureTopRank(String featureTopRank) {
this.featureTopRank = featureTopRank;
return this;
}

}

