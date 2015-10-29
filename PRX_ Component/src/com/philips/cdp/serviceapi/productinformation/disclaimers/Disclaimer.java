package com.philips.cdp.serviceapi.productinformation.disclaimers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Disclaimer {

@SerializedName("disclaimerText")
@Expose
private String disclaimerText;
@SerializedName("code")
@Expose
private String code;
@SerializedName("rank")
@Expose
private String rank;
@SerializedName("referenceName")
@Expose
private String referenceName;
@SerializedName("disclaimElements")
@Expose
private List<DisclaimElement> disclaimElements = new ArrayList<DisclaimElement>();

/**
* No args constructor for use in serialization
* 
*/
public Disclaimer() {
}

/**
* 
* @param disclaimerText
* @param rank
* @param disclaimElements
* @param code
* @param referenceName
*/
public Disclaimer(String disclaimerText, String code, String rank, String referenceName, List<DisclaimElement> disclaimElements) {
this.disclaimerText = disclaimerText;
this.code = code;
this.rank = rank;
this.referenceName = referenceName;
this.disclaimElements = disclaimElements;
}

/**
* 
* @return
* The disclaimerText
*/
public String getDisclaimerText() {
return disclaimerText;
}

/**
* 
* @param disclaimerText
* The disclaimerText
*/
public void setDisclaimerText(String disclaimerText) {
this.disclaimerText = disclaimerText;
}

public Disclaimer withDisclaimerText(String disclaimerText) {
this.disclaimerText = disclaimerText;
return this;
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

public Disclaimer withCode(String code) {
this.code = code;
return this;
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

public Disclaimer withRank(String rank) {
this.rank = rank;
return this;
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

public Disclaimer withReferenceName(String referenceName) {
this.referenceName = referenceName;
return this;
}

/**
* 
* @return
* The disclaimElements
*/
public List<DisclaimElement> getDisclaimElements() {
return disclaimElements;
}

/**
* 
* @param disclaimElements
* The disclaimElements
*/
public void setDisclaimElements(List<DisclaimElement> disclaimElements) {
this.disclaimElements = disclaimElements;
}

public Disclaimer withDisclaimElements(List<DisclaimElement> disclaimElements) {
this.disclaimElements = disclaimElements;
return this;
}

}

