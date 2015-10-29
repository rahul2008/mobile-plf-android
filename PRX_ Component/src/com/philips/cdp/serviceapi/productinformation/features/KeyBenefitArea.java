package com.philips.cdp.serviceapi.productinformation.features;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class KeyBenefitArea {

@SerializedName("keyBenefitAreaCode")
@Expose
private String keyBenefitAreaCode;
@SerializedName("keyBenefitAreaName")
@Expose
private String keyBenefitAreaName;
@SerializedName("keyBenefitAreaRank")
@Expose
private String keyBenefitAreaRank;
@SerializedName("feature")
@Expose
private List<Feature> feature = new ArrayList<Feature>();

/**
* No args constructor for use in serialization
* 
*/
public KeyBenefitArea() {
}

/**
* 
* @param keyBenefitAreaRank
* @param feature
* @param keyBenefitAreaName
* @param keyBenefitAreaCode
*/
public KeyBenefitArea(String keyBenefitAreaCode, String keyBenefitAreaName, String keyBenefitAreaRank, List<Feature> feature) {
this.keyBenefitAreaCode = keyBenefitAreaCode;
this.keyBenefitAreaName = keyBenefitAreaName;
this.keyBenefitAreaRank = keyBenefitAreaRank;
this.feature = feature;
}

/**
* 
* @return
* The keyBenefitAreaCode
*/
public String getKeyBenefitAreaCode() {
return keyBenefitAreaCode;
}

/**
* 
* @param keyBenefitAreaCode
* The keyBenefitAreaCode
*/
public void setKeyBenefitAreaCode(String keyBenefitAreaCode) {
this.keyBenefitAreaCode = keyBenefitAreaCode;
}

public KeyBenefitArea withKeyBenefitAreaCode(String keyBenefitAreaCode) {
this.keyBenefitAreaCode = keyBenefitAreaCode;
return this;
}

/**
* 
* @return
* The keyBenefitAreaName
*/
public String getKeyBenefitAreaName() {
return keyBenefitAreaName;
}

/**
* 
* @param keyBenefitAreaName
* The keyBenefitAreaName
*/
public void setKeyBenefitAreaName(String keyBenefitAreaName) {
this.keyBenefitAreaName = keyBenefitAreaName;
}

public KeyBenefitArea withKeyBenefitAreaName(String keyBenefitAreaName) {
this.keyBenefitAreaName = keyBenefitAreaName;
return this;
}

/**
* 
* @return
* The keyBenefitAreaRank
*/
public String getKeyBenefitAreaRank() {
return keyBenefitAreaRank;
}

/**
* 
* @param keyBenefitAreaRank
* The keyBenefitAreaRank
*/
public void setKeyBenefitAreaRank(String keyBenefitAreaRank) {
this.keyBenefitAreaRank = keyBenefitAreaRank;
}

public KeyBenefitArea withKeyBenefitAreaRank(String keyBenefitAreaRank) {
this.keyBenefitAreaRank = keyBenefitAreaRank;
return this;
}

/**
* 
* @return
* The feature
*/
public List<Feature> getFeature() {
return feature;
}

/**
* 
* @param feature
* The feature
*/
public void setFeature(List<Feature> feature) {
this.feature = feature;
}

public KeyBenefitArea withFeature(List<Feature> feature) {
this.feature = feature;
return this;
}

}
