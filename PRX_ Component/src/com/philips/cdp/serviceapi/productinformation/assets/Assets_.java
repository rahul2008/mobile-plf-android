package com.philips.cdp.serviceapi.productinformation.assets;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Assets_ {

@SerializedName("asset")
@Expose
private List<Asset> asset = new ArrayList<Asset>();

/**
* No args constructor for use in serialization
* 
*/
public Assets_() {
}

/**
* 
* @param asset
*/
public Assets_(List<Asset> asset) {
this.asset = asset;
}

/**
* 
* @return
* The asset
*/
public List<Asset> getAsset() {
return asset;
}

/**
* 
* @param asset
* The asset
*/
public void setAsset(List<Asset> asset) {
this.asset = asset;
}

public Assets_ withAsset(List<Asset> asset) {
this.asset = asset;
return this;
}

}
