package com.philips.cdp.serviceapi.productinformation.features;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

@SerializedName("keyBenefitArea")
@Expose
private List<KeyBenefitArea> keyBenefitArea = new ArrayList<KeyBenefitArea>();
@SerializedName("featureHighlight")
@Expose
private List<FeatureHighlight> featureHighlight = new ArrayList<FeatureHighlight>();
@SerializedName("code")
@Expose
private List<Code> code = new ArrayList<Code>();

/**
* No args constructor for use in serialization
* 
*/
public Data() {
}

/**
* 
* @param featureHighlight
* @param keyBenefitArea
* @param code
*/
public Data(List<KeyBenefitArea> keyBenefitArea, List<FeatureHighlight> featureHighlight, List<Code> code) {
this.keyBenefitArea = keyBenefitArea;
this.featureHighlight = featureHighlight;
this.code = code;
}

/**
* 
* @return
* The keyBenefitArea
*/
public List<KeyBenefitArea> getKeyBenefitArea() {
return keyBenefitArea;
}

/**
* 
* @param keyBenefitArea
* The keyBenefitArea
*/
public void setKeyBenefitArea(List<KeyBenefitArea> keyBenefitArea) {
this.keyBenefitArea = keyBenefitArea;
}

public Data withKeyBenefitArea(List<KeyBenefitArea> keyBenefitArea) {
this.keyBenefitArea = keyBenefitArea;
return this;
}

/**
* 
* @return
* The featureHighlight
*/
public List<FeatureHighlight> getFeatureHighlight() {
return featureHighlight;
}

/**
* 
* @param featureHighlight
* The featureHighlight
*/
public void setFeatureHighlight(List<FeatureHighlight> featureHighlight) {
this.featureHighlight = featureHighlight;
}

public Data withFeatureHighlight(List<FeatureHighlight> featureHighlight) {
this.featureHighlight = featureHighlight;
return this;
}

/**
* 
* @return
* The code
*/
public List<Code> getCode() {
return code;
}

/**
* 
* @param code
* The code
*/
public void setCode(List<Code> code) {
this.code = code;
}

public Data withCode(List<Code> code) {
this.code = code;
return this;
}

}

