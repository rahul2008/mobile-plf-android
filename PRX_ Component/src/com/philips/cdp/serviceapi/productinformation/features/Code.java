package com.philips.cdp.serviceapi.productinformation.features;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Code {

@SerializedName("code")
@Expose
private String code;
@SerializedName("description")
@Expose
private String description;
@SerializedName("extension")
@Expose
private String extension;
@SerializedName("extent")
@Expose
private String extent;
@SerializedName("lastModified")
@Expose
private String lastModified;
@SerializedName("locale")
@Expose
private String locale;
@SerializedName("number")
@Expose
private String number;
@SerializedName("type")
@Expose
private String type;
@SerializedName("asset")
@Expose
private String asset;

/**
* No args constructor for use in serialization
* 
*/
public Code() {
}

/**
* 
* @param extension
* @param asset
* @param lastModified
* @param extent
* @param description
* @param locale
* @param number
* @param code
* @param type
*/
public Code(String code, String description, String extension, String extent, String lastModified, String locale, String number, String type, String asset) {
this.code = code;
this.description = description;
this.extension = extension;
this.extent = extent;
this.lastModified = lastModified;
this.locale = locale;
this.number = number;
this.type = type;
this.asset = asset;
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

public Code withCode(String code) {
this.code = code;
return this;
}

/**
* 
* @return
* The description
*/
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
public void setDescription(String description) {
this.description = description;
}

public Code withDescription(String description) {
this.description = description;
return this;
}

/**
* 
* @return
* The extension
*/
public String getExtension() {
return extension;
}

/**
* 
* @param extension
* The extension
*/
public void setExtension(String extension) {
this.extension = extension;
}

public Code withExtension(String extension) {
this.extension = extension;
return this;
}

/**
* 
* @return
* The extent
*/
public String getExtent() {
return extent;
}

/**
* 
* @param extent
* The extent
*/
public void setExtent(String extent) {
this.extent = extent;
}

public Code withExtent(String extent) {
this.extent = extent;
return this;
}

/**
* 
* @return
* The lastModified
*/
public String getLastModified() {
return lastModified;
}

/**
* 
* @param lastModified
* The lastModified
*/
public void setLastModified(String lastModified) {
this.lastModified = lastModified;
}

public Code withLastModified(String lastModified) {
this.lastModified = lastModified;
return this;
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

public Code withLocale(String locale) {
this.locale = locale;
return this;
}

/**
* 
* @return
* The number
*/
public String getNumber() {
return number;
}

/**
* 
* @param number
* The number
*/
public void setNumber(String number) {
this.number = number;
}

public Code withNumber(String number) {
this.number = number;
return this;
}

/**
* 
* @return
* The type
*/
public String getType() {
return type;
}

/**
* 
* @param type
* The type
*/
public void setType(String type) {
this.type = type;
}

public Code withType(String type) {
this.type = type;
return this;
}

/**
* 
* @return
* The asset
*/
public String getAsset() {
return asset;
}

/**
* 
* @param asset
* The asset
*/
public void setAsset(String asset) {
this.asset = asset;
}

public Code withAsset(String asset) {
this.asset = asset;
return this;
}

}

