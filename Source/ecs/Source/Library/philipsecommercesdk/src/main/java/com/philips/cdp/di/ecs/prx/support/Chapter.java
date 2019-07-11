package com.philips.cdp.di.ecs.prx.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Chapter {

@SerializedName("name")
@Expose
private String name;
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
*
* @return
* The name
*/
public String getName() {
return name;
}

/**
*
* @param name
* The name
*/
public void setName(String name) {
this.name = name;
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

}
