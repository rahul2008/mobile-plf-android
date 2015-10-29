package com.philips.cdp.serviceapi.productinformation.disclaimers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

@SerializedName("disclaimers")
@Expose
private Disclaimers_ disclaimers;

/**
* No args constructor for use in serialization
* 
*/
public Data() {
}

/**
* 
* @param disclaimers
*/
public Data(Disclaimers_ disclaimers) {
this.disclaimers = disclaimers;
}

/**
* 
* @return
* The disclaimers
*/
public Disclaimers_ getDisclaimers() {
return disclaimers;
}

/**
* 
* @param disclaimers
* The disclaimers
*/
public void setDisclaimers(Disclaimers_ disclaimers) {
this.disclaimers = disclaimers;
}

public Data withDisclaimers(Disclaimers_ disclaimers) {
this.disclaimers = disclaimers;
return this;
}

}

