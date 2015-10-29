package com.philips.cdp.serviceapi.productinformation.specification;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CsItem {

@SerializedName("csItemCode")
@Expose
private String csItemCode;
@SerializedName("csItemName")
@Expose
private String csItemName;
@SerializedName("csItemRank")
@Expose
private String csItemRank;
@SerializedName("csItemIsFreeFormat")
@Expose
private String csItemIsFreeFormat;
@SerializedName("csValue")
@Expose
private List<CsValue> csValue = new ArrayList<CsValue>();

/**
* No args constructor for use in serialization
* 
*/
public CsItem() {
}

/**
* 
* @param csItemCode
* @param csItemIsFreeFormat
* @param csItemName
* @param csValue
* @param csItemRank
*/
public CsItem(String csItemCode, String csItemName, String csItemRank, String csItemIsFreeFormat, List<CsValue> csValue) {
this.csItemCode = csItemCode;
this.csItemName = csItemName;
this.csItemRank = csItemRank;
this.csItemIsFreeFormat = csItemIsFreeFormat;
this.csValue = csValue;
}

/**
* 
* @return
* The csItemCode
*/
public String getCsItemCode() {
return csItemCode;
}

/**
* 
* @param csItemCode
* The csItemCode
*/
public void setCsItemCode(String csItemCode) {
this.csItemCode = csItemCode;
}

public CsItem withCsItemCode(String csItemCode) {
this.csItemCode = csItemCode;
return this;
}

/**
* 
* @return
* The csItemName
*/
public String getCsItemName() {
return csItemName;
}

/**
* 
* @param csItemName
* The csItemName
*/
public void setCsItemName(String csItemName) {
this.csItemName = csItemName;
}

public CsItem withCsItemName(String csItemName) {
this.csItemName = csItemName;
return this;
}

/**
* 
* @return
* The csItemRank
*/
public String getCsItemRank() {
return csItemRank;
}

/**
* 
* @param csItemRank
* The csItemRank
*/
public void setCsItemRank(String csItemRank) {
this.csItemRank = csItemRank;
}

public CsItem withCsItemRank(String csItemRank) {
this.csItemRank = csItemRank;
return this;
}

/**
* 
* @return
* The csItemIsFreeFormat
*/
public String getCsItemIsFreeFormat() {
return csItemIsFreeFormat;
}

/**
* 
* @param csItemIsFreeFormat
* The csItemIsFreeFormat
*/
public void setCsItemIsFreeFormat(String csItemIsFreeFormat) {
this.csItemIsFreeFormat = csItemIsFreeFormat;
}

public CsItem withCsItemIsFreeFormat(String csItemIsFreeFormat) {
this.csItemIsFreeFormat = csItemIsFreeFormat;
return this;
}

/**
* 
* @return
* The csValue
*/
public List<CsValue> getCsValue() {
return csValue;
}

/**
* 
* @param csValue
* The csValue
*/
public void setCsValue(List<CsValue> csValue) {
this.csValue = csValue;
}

public CsItem withCsValue(List<CsValue> csValue) {
this.csValue = csValue;
return this;
}

}

