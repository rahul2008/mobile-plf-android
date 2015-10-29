package com.philips.cdp.serviceapi.productinformation.specification;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CsItems {

@SerializedName("csItem")
@Expose
private List<CsItem_> csItem = new ArrayList<CsItem_>();

/**
* No args constructor for use in serialization
* 
*/
public CsItems() {
}

/**
* 
* @param csItem
*/
public CsItems(List<CsItem_> csItem) {
this.csItem = csItem;
}

/**
* 
* @return
* The csItem
*/
public List<CsItem_> getCsItem() {
return csItem;
}

/**
* 
* @param csItem
* The csItem
*/
public void setCsItem(List<CsItem_> csItem) {
this.csItem = csItem;
}

public CsItems withCsItem(List<CsItem_> csItem) {
this.csItem = csItem;
return this;
}

}

