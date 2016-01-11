package com.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FilterDisplayValue {

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
public FilterDisplayValue() {
}

/**
* 
* @param filterKeyValue
* @param filterKey
*/
public FilterDisplayValue(String filterKey, String filterKeyValue) {
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

public FilterDisplayValue withFilterKey(String filterKey) {
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

public FilterDisplayValue withFilterKeyValue(String filterKeyValue) {
this.filterKeyValue = filterKeyValue;
return this;
}

}
