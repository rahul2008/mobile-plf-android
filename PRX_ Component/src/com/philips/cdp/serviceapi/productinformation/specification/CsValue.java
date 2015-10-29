package com.philips.cdp.serviceapi.productinformation.specification;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CsValue {

@SerializedName("csValueCode")
@Expose
private String csValueCode;
@SerializedName("csValueName")
@Expose
private String csValueName;
@SerializedName("csValueRank")
@Expose
private String csValueRank;

/**
* No args constructor for use in serialization
* 
*/
public CsValue() {
}

/**
* 
* @param csValueCode
* @param csValueRank
* @param csValueName
*/
public CsValue(String csValueCode, String csValueName, String csValueRank) {
this.csValueCode = csValueCode;
this.csValueName = csValueName;
this.csValueRank = csValueRank;
}

/**
* 
* @return
* The csValueCode
*/
public String getCsValueCode() {
return csValueCode;
}

/**
* 
* @param csValueCode
* The csValueCode
*/
public void setCsValueCode(String csValueCode) {
this.csValueCode = csValueCode;
}

public CsValue withCsValueCode(String csValueCode) {
this.csValueCode = csValueCode;
return this;
}

/**
* 
* @return
* The csValueName
*/
public String getCsValueName() {
return csValueName;
}

/**
* 
* @param csValueName
* The csValueName
*/
public void setCsValueName(String csValueName) {
this.csValueName = csValueName;
}

public CsValue withCsValueName(String csValueName) {
this.csValueName = csValueName;
return this;
}

/**
* 
* @return
* The csValueRank
*/
public String getCsValueRank() {
return csValueRank;
}

/**
* 
* @param csValueRank
* The csValueRank
*/
public void setCsValueRank(String csValueRank) {
this.csValueRank = csValueRank;
}

public CsValue withCsValueRank(String csValueRank) {
this.csValueRank = csValueRank;
return this;
}

}

