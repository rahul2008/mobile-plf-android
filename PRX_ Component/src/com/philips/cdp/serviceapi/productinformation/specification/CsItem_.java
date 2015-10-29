package com.philips.cdp.serviceapi.productinformation.specification;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CsItem_ {

@SerializedName("code")
@Expose
private String code;
@SerializedName("rank")
@Expose
private String rank;
@SerializedName("referenceName")
@Expose
private String referenceName;

/**
* No args constructor for use in serialization
* 
*/
public CsItem_() {
}

/**
* 
* @param rank
* @param code
* @param referenceName
*/
public CsItem_(String code, String rank, String referenceName) {
this.code = code;
this.rank = rank;
this.referenceName = referenceName;
}

/**
* 
* @return
* The code
*/
public String getCode() {
return code;
}

/**
* 
* @param code
* The code
*/
public void setCode(String code) {
this.code = code;
}

public CsItem_ withCode(String code) {
this.code = code;
return this;
}

/**
* 
* @return
* The rank
*/
public String getRank() {
return rank;
}

/**
* 
* @param rank
* The rank
*/
public void setRank(String rank) {
this.rank = rank;
}

public CsItem_ withRank(String rank) {
this.rank = rank;
return this;
}

/**
* 
* @return
* The referenceName
*/
public String getReferenceName() {
return referenceName;
}

/**
* 
* @param referenceName
* The referenceName
*/
public void setReferenceName(String referenceName) {
this.referenceName = referenceName;
}

public CsItem_ withReferenceName(String referenceName) {
this.referenceName = referenceName;
return this;
}

}

