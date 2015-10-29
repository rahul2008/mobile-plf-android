package com.philips.cdp.serviceapi.productinformation.specification;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

@SerializedName("csChapter")
@Expose
private List<CsChapter> csChapter = new ArrayList<CsChapter>();
@SerializedName("filters")
@Expose
private Filters filters;

/**
* No args constructor for use in serialization
* 
*/
public Data() {
}

/**
* 
* @param csChapter
* @param filters
*/
public Data(List<CsChapter> csChapter, Filters filters) {
this.csChapter = csChapter;
this.filters = filters;
}

/**
* 
* @return
* The csChapter
*/
public List<CsChapter> getCsChapter() {
return csChapter;
}

/**
* 
* @param csChapter
* The csChapter
*/
public void setCsChapter(List<CsChapter> csChapter) {
this.csChapter = csChapter;
}

public Data withCsChapter(List<CsChapter> csChapter) {
this.csChapter = csChapter;
return this;
}

/**
* 
* @return
* The filters
*/
public Filters getFilters() {
return filters;
}

/**
* 
* @param filters
* The filters
*/
public void setFilters(Filters filters) {
this.filters = filters;
}

public Data withFilters(Filters filters) {
this.filters = filters;
return this;
}

}

