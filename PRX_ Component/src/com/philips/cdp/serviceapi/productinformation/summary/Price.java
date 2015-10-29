package com.philips.cdp.serviceapi.productinformation.summary;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Price {

@SerializedName("productPrice")
@Expose
private String productPrice;
@SerializedName("displayPriceType")
@Expose
private String displayPriceType;
@SerializedName("displayPrice")
@Expose
private String displayPrice;
@SerializedName("currencyCode")
@Expose
private String currencyCode;
@SerializedName("formattedPrice")
@Expose
private String formattedPrice;
@SerializedName("formattedDisplayPrice")
@Expose
private String formattedDisplayPrice;

/**
* 
* @return
* The productPrice
*/
public String getProductPrice() {
return productPrice;
}

/**
* 
* @param productPrice
* The productPrice
*/
public void setProductPrice(String productPrice) {
this.productPrice = productPrice;
}

/**
* 
* @return
* The displayPriceType
*/
public String getDisplayPriceType() {
return displayPriceType;
}

/**
* 
* @param displayPriceType
* The displayPriceType
*/
public void setDisplayPriceType(String displayPriceType) {
this.displayPriceType = displayPriceType;
}

/**
* 
* @return
* The displayPrice
*/
public String getDisplayPrice() {
return displayPrice;
}

/**
* 
* @param displayPrice
* The displayPrice
*/
public void setDisplayPrice(String displayPrice) {
this.displayPrice = displayPrice;
}

/**
* 
* @return
* The currencyCode
*/
public String getCurrencyCode() {
return currencyCode;
}

/**
* 
* @param currencyCode
* The currencyCode
*/
public void setCurrencyCode(String currencyCode) {
this.currencyCode = currencyCode;
}

/**
* 
* @return
* The formattedPrice
*/
public String getFormattedPrice() {
return formattedPrice;
}

/**
* 
* @param formattedPrice
* The formattedPrice
*/
public void setFormattedPrice(String formattedPrice) {
this.formattedPrice = formattedPrice;
}

/**
* 
* @return
* The formattedDisplayPrice
*/
public String getFormattedDisplayPrice() {
return formattedDisplayPrice;
}

/**
* 
* @param formattedDisplayPrice
* The formattedDisplayPrice
*/
public void setFormattedDisplayPrice(String formattedDisplayPrice) {
this.formattedDisplayPrice = formattedDisplayPrice;
}

}

