package com.philips.cdp.serviceapi.productinformation.overview;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

@SerializedName("productTitle")
@Expose
private String productTitle;
@SerializedName("wow")
@Expose
private String wow;
@SerializedName("subWOW")
@Expose
private String subWOW;
@SerializedName("marketingTextHeader")
@Expose
private String marketingTextHeader;
@SerializedName("imageURL")
@Expose
private String imageURL;
@SerializedName("reviewStatistics")
@Expose
private ReviewStatistics reviewStatistics;
@SerializedName("price")
@Expose
private Price price;

/**
* No args constructor for use in serialization
* 
*/
public Data() {
}

/**
* 
* @param wow
* @param price
* @param productTitle
* @param subWOW
* @param reviewStatistics
* @param marketingTextHeader
* @param imageURL
*/
public Data(String productTitle, String wow, String subWOW, String marketingTextHeader, String imageURL, ReviewStatistics reviewStatistics, Price price) {
this.productTitle = productTitle;
this.wow = wow;
this.subWOW = subWOW;
this.marketingTextHeader = marketingTextHeader;
this.imageURL = imageURL;
this.reviewStatistics = reviewStatistics;
this.price = price;
}

/**
* 
* @return
* The productTitle
*/
public String getProductTitle() {
return productTitle;
}

/**
* 
* @param productTitle
* The productTitle
*/
public void setProductTitle(String productTitle) {
this.productTitle = productTitle;
}

public Data withProductTitle(String productTitle) {
this.productTitle = productTitle;
return this;
}

/**
* 
* @return
* The wow
*/
public String getWow() {
return wow;
}

/**
* 
* @param wow
* The wow
*/
public void setWow(String wow) {
this.wow = wow;
}

public Data withWow(String wow) {
this.wow = wow;
return this;
}

/**
* 
* @return
* The subWOW
*/
public String getSubWOW() {
return subWOW;
}

/**
* 
* @param subWOW
* The subWOW
*/
public void setSubWOW(String subWOW) {
this.subWOW = subWOW;
}

public Data withSubWOW(String subWOW) {
this.subWOW = subWOW;
return this;
}

/**
* 
* @return
* The marketingTextHeader
*/
public String getMarketingTextHeader() {
return marketingTextHeader;
}

/**
* 
* @param marketingTextHeader
* The marketingTextHeader
*/
public void setMarketingTextHeader(String marketingTextHeader) {
this.marketingTextHeader = marketingTextHeader;
}

public Data withMarketingTextHeader(String marketingTextHeader) {
this.marketingTextHeader = marketingTextHeader;
return this;
}

/**
* 
* @return
* The imageURL
*/
public String getImageURL() {
return imageURL;
}

/**
* 
* @param imageURL
* The imageURL
*/
public void setImageURL(String imageURL) {
this.imageURL = imageURL;
}

public Data withImageURL(String imageURL) {
this.imageURL = imageURL;
return this;
}

/**
* 
* @return
* The reviewStatistics
*/
public ReviewStatistics getReviewStatistics() {
return reviewStatistics;
}

/**
* 
* @param reviewStatistics
* The reviewStatistics
*/
public void setReviewStatistics(ReviewStatistics reviewStatistics) {
this.reviewStatistics = reviewStatistics;
}

public Data withReviewStatistics(ReviewStatistics reviewStatistics) {
this.reviewStatistics = reviewStatistics;
return this;
}

/**
* 
* @return
* The price
*/
public Price getPrice() {
return price;
}

/**
* 
* @param price
* The price
*/
public void setPrice(Price price) {
this.price = price;
}

public Data withPrice(Price price) {
this.price = price;
return this;
}

}