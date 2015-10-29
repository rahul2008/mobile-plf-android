package com.philips.cdp.serviceapi.productinformation.accessories;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Datum {

@SerializedName("locale")
@Expose
private String locale;
@SerializedName("ctn")
@Expose
private String ctn;
@SerializedName("dtn")
@Expose
private String dtn;
@SerializedName("leafletUrl")
@Expose
private String leafletUrl;
@SerializedName("productTitle")
@Expose
private String productTitle;
@SerializedName("alphanumeric")
@Expose
private String alphanumeric;
@SerializedName("brandName")
@Expose
private String brandName;
@SerializedName("brand")
@Expose
private Brand brand;
@SerializedName("productURL")
@Expose
private String productURL;
@SerializedName("productPagePath")
@Expose
private String productPagePath;
@SerializedName("descriptor")
@Expose
private String descriptor;
@SerializedName("domain")
@Expose
private String domain;
@SerializedName("versions")
@Expose
private List<String> versions = new ArrayList<String>();
@SerializedName("productStatus")
@Expose
private String productStatus;
@SerializedName("imageURL")
@Expose
private String imageURL;
@SerializedName("sop")
@Expose
private String sop;
@SerializedName("somp")
@Expose
private String somp;
@SerializedName("eop")
@Expose
private String eop;
@SerializedName("isDeleted")
@Expose
private boolean isDeleted;
@SerializedName("priority")
@Expose
private long priority;
@SerializedName("price")
@Expose
private Price price;
@SerializedName("wow")
@Expose
private String wow;
@SerializedName("productType")
@Expose
private String productType;
@SerializedName("careSop")
@Expose
private String careSop;
@SerializedName("filterKeys")
@Expose
private List<String> filterKeys = new ArrayList<String>();
@SerializedName("subcategory")
@Expose
private String subcategory;
@SerializedName("reviewStatistics")
@Expose
private ReviewStatistics reviewStatistics;
@SerializedName("code12NC")
@Expose
private String code12NC;
@SerializedName("familyName")
@Expose
private String familyName;

/**
* No args constructor for use in serialization
* 
*/
public Datum() {
}

/**
* 
* @param descriptor
* @param locale
* @param familyName
* @param code12NC
* @param alphanumeric
* @param brandName
* @param productType
* @param dtn
* @param wow
* @param productStatus
* @param priority
* @param domain
* @param leafletUrl
* @param eop
* @param careSop
* @param ctn
* @param productPagePath
* @param reviewStatistics
* @param sop
* @param isDeleted
* @param filterKeys
* @param versions
* @param price
* @param subcategory
* @param somp
* @param productTitle
* @param brand
* @param imageURL
* @param productURL
*/
public Datum(String locale, String ctn, String dtn, String leafletUrl, String productTitle, String alphanumeric, String brandName, Brand brand, String productURL, String productPagePath, String descriptor, String domain, List<String> versions, String productStatus, String imageURL, String sop, String somp, String eop, boolean isDeleted, long priority, Price price, String wow, String productType, String careSop, List<String> filterKeys, String subcategory, ReviewStatistics reviewStatistics, String code12NC, String familyName) {
this.locale = locale;
this.ctn = ctn;
this.dtn = dtn;
this.leafletUrl = leafletUrl;
this.productTitle = productTitle;
this.alphanumeric = alphanumeric;
this.brandName = brandName;
this.brand = brand;
this.productURL = productURL;
this.productPagePath = productPagePath;
this.descriptor = descriptor;
this.domain = domain;
this.versions = versions;
this.productStatus = productStatus;
this.imageURL = imageURL;
this.sop = sop;
this.somp = somp;
this.eop = eop;
this.isDeleted = isDeleted;
this.priority = priority;
this.price = price;
this.wow = wow;
this.productType = productType;
this.careSop = careSop;
this.filterKeys = filterKeys;
this.subcategory = subcategory;
this.reviewStatistics = reviewStatistics;
this.code12NC = code12NC;
this.familyName = familyName;
}

/**
* 
* @return
* The locale
*/
public String getLocale() {
return locale;
}

/**
* 
* @param locale
* The locale
*/
public void setLocale(String locale) {
this.locale = locale;
}

public Datum withLocale(String locale) {
this.locale = locale;
return this;
}

/**
* 
* @return
* The ctn
*/
public String getCtn() {
return ctn;
}

/**
* 
* @param ctn
* The ctn
*/
public void setCtn(String ctn) {
this.ctn = ctn;
}

public Datum withCtn(String ctn) {
this.ctn = ctn;
return this;
}

/**
* 
* @return
* The dtn
*/
public String getDtn() {
return dtn;
}

/**
* 
* @param dtn
* The dtn
*/
public void setDtn(String dtn) {
this.dtn = dtn;
}

public Datum withDtn(String dtn) {
this.dtn = dtn;
return this;
}

/**
* 
* @return
* The leafletUrl
*/
public String getLeafletUrl() {
return leafletUrl;
}

/**
* 
* @param leafletUrl
* The leafletUrl
*/
public void setLeafletUrl(String leafletUrl) {
this.leafletUrl = leafletUrl;
}

public Datum withLeafletUrl(String leafletUrl) {
this.leafletUrl = leafletUrl;
return this;
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

public Datum withProductTitle(String productTitle) {
this.productTitle = productTitle;
return this;
}

/**
* 
* @return
* The alphanumeric
*/
public String getAlphanumeric() {
return alphanumeric;
}

/**
* 
* @param alphanumeric
* The alphanumeric
*/
public void setAlphanumeric(String alphanumeric) {
this.alphanumeric = alphanumeric;
}

public Datum withAlphanumeric(String alphanumeric) {
this.alphanumeric = alphanumeric;
return this;
}

/**
* 
* @return
* The brandName
*/
public String getBrandName() {
return brandName;
}

/**
* 
* @param brandName
* The brandName
*/
public void setBrandName(String brandName) {
this.brandName = brandName;
}

public Datum withBrandName(String brandName) {
this.brandName = brandName;
return this;
}

/**
* 
* @return
* The brand
*/
public Brand getBrand() {
return brand;
}

/**
* 
* @param brand
* The brand
*/
public void setBrand(Brand brand) {
this.brand = brand;
}

public Datum withBrand(Brand brand) {
this.brand = brand;
return this;
}

/**
* 
* @return
* The productURL
*/
public String getProductURL() {
return productURL;
}

/**
* 
* @param productURL
* The productURL
*/
public void setProductURL(String productURL) {
this.productURL = productURL;
}

public Datum withProductURL(String productURL) {
this.productURL = productURL;
return this;
}

/**
* 
* @return
* The productPagePath
*/
public String getProductPagePath() {
return productPagePath;
}

/**
* 
* @param productPagePath
* The productPagePath
*/
public void setProductPagePath(String productPagePath) {
this.productPagePath = productPagePath;
}

public Datum withProductPagePath(String productPagePath) {
this.productPagePath = productPagePath;
return this;
}

/**
* 
* @return
* The descriptor
*/
public String getDescriptor() {
return descriptor;
}

/**
* 
* @param descriptor
* The descriptor
*/
public void setDescriptor(String descriptor) {
this.descriptor = descriptor;
}

public Datum withDescriptor(String descriptor) {
this.descriptor = descriptor;
return this;
}

/**
* 
* @return
* The domain
*/
public String getDomain() {
return domain;
}

/**
* 
* @param domain
* The domain
*/
public void setDomain(String domain) {
this.domain = domain;
}

public Datum withDomain(String domain) {
this.domain = domain;
return this;
}

/**
* 
* @return
* The versions
*/
public List<String> getVersions() {
return versions;
}

/**
* 
* @param versions
* The versions
*/
public void setVersions(List<String> versions) {
this.versions = versions;
}

public Datum withVersions(List<String> versions) {
this.versions = versions;
return this;
}

/**
* 
* @return
* The productStatus
*/
public String getProductStatus() {
return productStatus;
}

/**
* 
* @param productStatus
* The productStatus
*/
public void setProductStatus(String productStatus) {
this.productStatus = productStatus;
}

public Datum withProductStatus(String productStatus) {
this.productStatus = productStatus;
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

public Datum withImageURL(String imageURL) {
this.imageURL = imageURL;
return this;
}

/**
* 
* @return
* The sop
*/
public String getSop() {
return sop;
}

/**
* 
* @param sop
* The sop
*/
public void setSop(String sop) {
this.sop = sop;
}

public Datum withSop(String sop) {
this.sop = sop;
return this;
}

/**
* 
* @return
* The somp
*/
public String getSomp() {
return somp;
}

/**
* 
* @param somp
* The somp
*/
public void setSomp(String somp) {
this.somp = somp;
}

public Datum withSomp(String somp) {
this.somp = somp;
return this;
}

/**
* 
* @return
* The eop
*/
public String getEop() {
return eop;
}

/**
* 
* @param eop
* The eop
*/
public void setEop(String eop) {
this.eop = eop;
}

public Datum withEop(String eop) {
this.eop = eop;
return this;
}

/**
* 
* @return
* The isDeleted
*/
public boolean isIsDeleted() {
return isDeleted;
}

/**
* 
* @param isDeleted
* The isDeleted
*/
public void setIsDeleted(boolean isDeleted) {
this.isDeleted = isDeleted;
}

public Datum withIsDeleted(boolean isDeleted) {
this.isDeleted = isDeleted;
return this;
}

/**
* 
* @return
* The priority
*/
public long getPriority() {
return priority;
}

/**
* 
* @param priority
* The priority
*/
public void setPriority(long priority) {
this.priority = priority;
}

public Datum withPriority(long priority) {
this.priority = priority;
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

public Datum withPrice(Price price) {
this.price = price;
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

public Datum withWow(String wow) {
this.wow = wow;
return this;
}

/**
* 
* @return
* The productType
*/
public String getProductType() {
return productType;
}

/**
* 
* @param productType
* The productType
*/
public void setProductType(String productType) {
this.productType = productType;
}

public Datum withProductType(String productType) {
this.productType = productType;
return this;
}

/**
* 
* @return
* The careSop
*/
public String getCareSop() {
return careSop;
}

/**
* 
* @param careSop
* The careSop
*/
public void setCareSop(String careSop) {
this.careSop = careSop;
}

public Datum withCareSop(String careSop) {
this.careSop = careSop;
return this;
}

/**
* 
* @return
* The filterKeys
*/
public List<String> getFilterKeys() {
return filterKeys;
}

/**
* 
* @param filterKeys
* The filterKeys
*/
public void setFilterKeys(List<String> filterKeys) {
this.filterKeys = filterKeys;
}

public Datum withFilterKeys(List<String> filterKeys) {
this.filterKeys = filterKeys;
return this;
}

/**
* 
* @return
* The subcategory
*/
public String getSubcategory() {
return subcategory;
}

/**
* 
* @param subcategory
* The subcategory
*/
public void setSubcategory(String subcategory) {
this.subcategory = subcategory;
}

public Datum withSubcategory(String subcategory) {
this.subcategory = subcategory;
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

public Datum withReviewStatistics(ReviewStatistics reviewStatistics) {
this.reviewStatistics = reviewStatistics;
return this;
}

/**
* 
* @return
* The code12NC
*/
public String getCode12NC() {
return code12NC;
}

/**
* 
* @param code12NC
* The code12NC
*/
public void setCode12NC(String code12NC) {
this.code12NC = code12NC;
}

public Datum withCode12NC(String code12NC) {
this.code12NC = code12NC;
return this;
}

/**
* 
* @return
* The familyName
*/
public String getFamilyName() {
return familyName;
}

/**
* 
* @param familyName
* The familyName
*/
public void setFamilyName(String familyName) {
this.familyName = familyName;
}

public Datum withFamilyName(String familyName) {
this.familyName = familyName;
return this;
}

}

