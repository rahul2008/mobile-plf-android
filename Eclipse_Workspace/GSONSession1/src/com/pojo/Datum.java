package com.pojo;

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
@SerializedName("code12NC")
@Expose
private String code12NC;
@SerializedName("additionalCodes")
@Expose
private String additionalCodes;
@SerializedName("dtn")
@Expose
private String dtn;
@SerializedName("leafletUrl")
@Expose
private String leafletUrl;
@SerializedName("productTitle")
@Expose
private String productTitle;
@SerializedName("productName")
@Expose
private String productName;
@SerializedName("alphanumeric")
@Expose
private String alphanumeric;
@SerializedName("brandName")
@Expose
private String brandName;
@SerializedName("brand")
@Expose
private Brand brand;
@SerializedName("familyName")
@Expose
private String familyName;
@SerializedName("productURL")
@Expose
private String productURL;
@SerializedName("productPagePath")
@Expose
private String productPagePath;
@SerializedName("domain")
@Expose
private String domain;
@SerializedName("productStatus")
@Expose
private String productStatus;
@SerializedName("imageURL")
@Expose
private String imageURL;
@SerializedName("sop")
@Expose
private String sop;
@SerializedName("eop")
@Expose
private String eop;
@SerializedName("isDeleted")
@Expose
private boolean isDeleted;
@SerializedName("priority")
@Expose
private long priority;
@SerializedName("keyLogo")
@Expose
private KeyLogo keyLogo;
@SerializedName("wow")
@Expose
private String wow;
@SerializedName("filterKeys")
@Expose
private List<String> filterKeys = new ArrayList<String>();
@SerializedName("filterValues")
@Expose
private List<FilterValue> filterValues = new ArrayList<FilterValue>();
@SerializedName("filterDisplayValues")
@Expose
private List<FilterDisplayValue> filterDisplayValues = new ArrayList<FilterDisplayValue>();
@SerializedName("subcategory")
@Expose
private String subcategory;

/**
* No args constructor for use in serialization
* 
*/
public Datum() {
}

/**
* 
* @param additionalCodes
* @param locale
* @param familyName
* @param code12NC
* @param alphanumeric
* @param brandName
* @param dtn
* @param wow
* @param productStatus
* @param priority
* @param leafletUrl
* @param domain
* @param eop
* @param ctn
* @param productPagePath
* @param sop
* @param isDeleted
* @param filterKeys
* @param filterValues
* @param keyLogo
* @param subcategory
* @param productTitle
* @param brand
* @param productName
* @param filterDisplayValues
* @param imageURL
* @param productURL
*/
public Datum(String locale, String ctn, String code12NC, String additionalCodes, String dtn, String leafletUrl, String productTitle, String productName, String alphanumeric, String brandName, Brand brand, String familyName, String productURL, String productPagePath, String domain, String productStatus, String imageURL, String sop, String eop, boolean isDeleted, long priority, KeyLogo keyLogo, String wow, List<String> filterKeys, List<FilterValue> filterValues, List<FilterDisplayValue> filterDisplayValues, String subcategory) {
this.locale = locale;
this.ctn = ctn;
this.code12NC = code12NC;
this.additionalCodes = additionalCodes;
this.dtn = dtn;
this.leafletUrl = leafletUrl;
this.productTitle = productTitle;
this.productName = productName;
this.alphanumeric = alphanumeric;
this.brandName = brandName;
this.brand = brand;
this.familyName = familyName;
this.productURL = productURL;
this.productPagePath = productPagePath;
this.domain = domain;
this.productStatus = productStatus;
this.imageURL = imageURL;
this.sop = sop;
this.eop = eop;
this.isDeleted = isDeleted;
this.priority = priority;
this.keyLogo = keyLogo;
this.wow = wow;
this.filterKeys = filterKeys;
this.filterValues = filterValues;
this.filterDisplayValues = filterDisplayValues;
this.subcategory = subcategory;
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
* The additionalCodes
*/
public String getAdditionalCodes() {
return additionalCodes;
}

/**
* 
* @param additionalCodes
* The additionalCodes
*/
public void setAdditionalCodes(String additionalCodes) {
this.additionalCodes = additionalCodes;
}

public Datum withAdditionalCodes(String additionalCodes) {
this.additionalCodes = additionalCodes;
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
* The productName
*/
public String getProductName() {
return productName;
}

/**
* 
* @param productName
* The productName
*/
public void setProductName(String productName) {
this.productName = productName;
}

public Datum withProductName(String productName) {
this.productName = productName;
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
* The keyLogo
*/
public KeyLogo getKeyLogo() {
return keyLogo;
}

/**
* 
* @param keyLogo
* The keyLogo
*/
public void setKeyLogo(KeyLogo keyLogo) {
this.keyLogo = keyLogo;
}

public Datum withKeyLogo(KeyLogo keyLogo) {
this.keyLogo = keyLogo;
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
* The filterValues
*/
public List<FilterValue> getFilterValues() {
return filterValues;
}

/**
* 
* @param filterValues
* The filterValues
*/
public void setFilterValues(List<FilterValue> filterValues) {
this.filterValues = filterValues;
}

public Datum withFilterValues(List<FilterValue> filterValues) {
this.filterValues = filterValues;
return this;
}

/**
* 
* @return
* The filterDisplayValues
*/
public List<FilterDisplayValue> getFilterDisplayValues() {
return filterDisplayValues;
}

/**
* 
* @param filterDisplayValues
* The filterDisplayValues
*/
public void setFilterDisplayValues(List<FilterDisplayValue> filterDisplayValues) {
this.filterDisplayValues = filterDisplayValues;
}

public Datum withFilterDisplayValues(List<FilterDisplayValue> filterDisplayValues) {
this.filterDisplayValues = filterDisplayValues;
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

}
