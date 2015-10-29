package com.philips.cdp.serviceapi.productinformation.navigation;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

@SerializedName("overview")
@Expose
private String overview;
@SerializedName("specification")
@Expose
private String specification;
@SerializedName("reviews")
@Expose
private String reviews;
@SerializedName("awards")
@Expose
private String awards;
@SerializedName("expertReview")
@Expose
private String expertReview;
@SerializedName("parts")
@Expose
private String parts;
@SerializedName("accessories")
@Expose
private String accessories;
@SerializedName("support")
@Expose
private String support;
@SerializedName("downloads")
@Expose
private String downloads;

/**
* No args constructor for use in serialization
* 
*/
public Data() {
}

/**
* 
* @param expertReview
* @param reviews
* @param support
* @param overview
* @param awards
* @param specification
* @param parts
* @param accessories
* @param downloads
*/
public Data(String overview, String specification, String reviews, String awards, String expertReview, String parts, String accessories, String support, String downloads) {
this.overview = overview;
this.specification = specification;
this.reviews = reviews;
this.awards = awards;
this.expertReview = expertReview;
this.parts = parts;
this.accessories = accessories;
this.support = support;
this.downloads = downloads;
}

/**
* 
* @return
* The overview
*/
public String getOverview() {
return overview;
}

/**
* 
* @param overview
* The overview
*/
public void setOverview(String overview) {
this.overview = overview;
}

public Data withOverview(String overview) {
this.overview = overview;
return this;
}

/**
* 
* @return
* The specification
*/
public String getSpecification() {
return specification;
}

/**
* 
* @param specification
* The specification
*/
public void setSpecification(String specification) {
this.specification = specification;
}

public Data withSpecification(String specification) {
this.specification = specification;
return this;
}

/**
* 
* @return
* The reviews
*/
public String getReviews() {
return reviews;
}

/**
* 
* @param reviews
* The reviews
*/
public void setReviews(String reviews) {
this.reviews = reviews;
}

public Data withReviews(String reviews) {
this.reviews = reviews;
return this;
}

/**
* 
* @return
* The awards
*/
public String getAwards() {
return awards;
}

/**
* 
* @param awards
* The awards
*/
public void setAwards(String awards) {
this.awards = awards;
}

public Data withAwards(String awards) {
this.awards = awards;
return this;
}

/**
* 
* @return
* The expertReview
*/
public String getExpertReview() {
return expertReview;
}

/**
* 
* @param expertReview
* The expertReview
*/
public void setExpertReview(String expertReview) {
this.expertReview = expertReview;
}

public Data withExpertReview(String expertReview) {
this.expertReview = expertReview;
return this;
}

/**
* 
* @return
* The parts
*/
public String getParts() {
return parts;
}

/**
* 
* @param parts
* The parts
*/
public void setParts(String parts) {
this.parts = parts;
}

public Data withParts(String parts) {
this.parts = parts;
return this;
}

/**
* 
* @return
* The accessories
*/
public String getAccessories() {
return accessories;
}

/**
* 
* @param accessories
* The accessories
*/
public void setAccessories(String accessories) {
this.accessories = accessories;
}

public Data withAccessories(String accessories) {
this.accessories = accessories;
return this;
}

/**
* 
* @return
* The support
*/
public String getSupport() {
return support;
}

/**
* 
* @param support
* The support
*/
public void setSupport(String support) {
this.support = support;
}

public Data withSupport(String support) {
this.support = support;
return this;
}

/**
* 
* @return
* The downloads
*/
public String getDownloads() {
return downloads;
}

/**
* 
* @param downloads
* The downloads
*/
public void setDownloads(String downloads) {
this.downloads = downloads;
}

public Data withDownloads(String downloads) {
this.downloads = downloads;
return this;
}

}

