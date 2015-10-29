package com.philips.cdp.serviceapi.productinformation.specification;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Purpose {

@SerializedName("type")
@Expose
private String type;
@SerializedName("features")
@Expose
private Features features;
@SerializedName("csItems")
@Expose
private CsItems csItems;

/**
* No args constructor for use in serialization
* 
*/
public Purpose() {
}

/**
* 
* @param csItems
* @param features
* @param type
*/
public Purpose(String type, Features features, CsItems csItems) {
this.type = type;
this.features = features;
this.csItems = csItems;
}

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

public Purpose withType(String type) {
this.type = type;
return this;
}

/**
* 
* @return
* The features
*/
public Features getFeatures() {
return features;
}

/**
* 
* @param features
* The features
*/
public void setFeatures(Features features) {
this.features = features;
}

public Purpose withFeatures(Features features) {
this.features = features;
return this;
}

/**
* 
* @return
* The csItems
*/
public CsItems getCsItems() {
return csItems;
}

/**
* 
* @param csItems
* The csItems
*/
public void setCsItems(CsItems csItems) {
this.csItems = csItems;
}

public Purpose withCsItems(CsItems csItems) {
this.csItems = csItems;
return this;
}

}

