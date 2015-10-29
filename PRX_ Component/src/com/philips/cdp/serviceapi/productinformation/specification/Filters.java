package com.philips.cdp.serviceapi.productinformation.specification;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Filters {

@SerializedName("purpose")
@Expose
private List<Purpose> purpose = new ArrayList<Purpose>();

/**
* No args constructor for use in serialization
* 
*/
public Filters() {
}

/**
* 
* @param purpose
*/
public Filters(List<Purpose> purpose) {
this.purpose = purpose;
}

/**
* 
* @return
* The purpose
*/
public List<Purpose> getPurpose() {
return purpose;
}

/**
* 
* @param purpose
* The purpose
*/
public void setPurpose(List<Purpose> purpose) {
this.purpose = purpose;
}

public Filters withPurpose(List<Purpose> purpose) {
this.purpose = purpose;
return this;
}

}

