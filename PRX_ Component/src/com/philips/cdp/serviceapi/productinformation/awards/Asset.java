package com.philips.cdp.serviceapi.productinformation.awards;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Asset {

@SerializedName("asset")
@Expose
private String asset;
@SerializedName("type")
@Expose
private String type;

/**
* No args constructor for use in serialization
* 
*/
public Asset() {
}

/**
* 
* @param asset
* @param type
*/
public Asset(String asset, String type) {
this.asset = asset;
this.type = type;
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

public Asset withAsset(String asset) {
this.asset = asset;
return this;
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

public Asset withType(String type) {
this.type = type;
return this;
}

}

