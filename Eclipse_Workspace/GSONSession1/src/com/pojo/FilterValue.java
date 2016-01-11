package com.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FilterValue {

@SerializedName("filterKey")
@Expose
private String filterKey;
@SerializedName("filterKeyValue")
@Expose
private String filterKeyValue;

/**
* No args constructor for use in serialization
* 
*/
public FilterValue() {
}

/**
* 
* @param filterKeyValue
* @param filterKey
*/
public FilterValue(String filterKey, String filterKeyValue) {
this.filterKey = filterKey;
this.filterKeyValue = filterKeyValue;
}

/**
* 
* @return
* The filterKey
*/
public String getFilterKey() {
return filterKey;
}

/**
* 
* @param filterKey
* The filterKey
*/
public void setFilterKey(String filterKey) {
this.filterKey = filterKey;
}

public FilterValue withFilterKey(String filterKey) {
this.filterKey = filterKey;
return this;
}

/**
* 
* @return
* The filterKeyValue
*/
public String getFilterKeyValue() {
return filterKeyValue;
}

/**
* 
* @param filterKeyValue
* The filterKeyValue
*/
public void setFilterKeyValue(String filterKeyValue) {
this.filterKeyValue = filterKeyValue;
}

public FilterValue withFilterKeyValue(String filterKeyValue) {
this.filterKeyValue = filterKeyValue;
return this;
}

}
