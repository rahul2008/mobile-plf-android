package com.philips.cdp.serviceapi.productinformation.assets;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

@SerializedName("assets")
@Expose
private Assets_ assets;

/**
* No args constructor for use in serialization
* 
*/
public Data() {
}

/**
* 
* @param assets
*/
public Data(Assets_ assets) {
this.assets = assets;
}

/**
* 
* @return
* The assets
*/
public Assets_ getAssets() {
return assets;
}

/**
* 
* @param assets
* The assets
*/
public void setAssets(Assets_ assets) {
this.assets = assets;
}

public Data withAssets(Assets_ assets) {
this.assets = assets;
return this;
}

}