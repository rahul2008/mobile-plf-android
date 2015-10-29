package com.philips.cdp.serviceapi.productinformation.disclaimers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Disclaimers_ {

@SerializedName("disclaimer")
@Expose
private List<Disclaimer> disclaimer = new ArrayList<Disclaimer>();

/**
* No args constructor for use in serialization
* 
*/
public Disclaimers_() {
}

/**
* 
* @param disclaimer
*/
public Disclaimers_(List<Disclaimer> disclaimer) {
this.disclaimer = disclaimer;
}

/**
* 
* @return
* The disclaimer
*/
public List<Disclaimer> getDisclaimer() {
return disclaimer;
}

/**
* 
* @param disclaimer
* The disclaimer
*/
public void setDisclaimer(List<Disclaimer> disclaimer) {
this.disclaimer = disclaimer;
}

public Disclaimers_ withDisclaimer(List<Disclaimer> disclaimer) {
this.disclaimer = disclaimer;
return this;
}

}