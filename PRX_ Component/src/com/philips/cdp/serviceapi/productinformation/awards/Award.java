package com.philips.cdp.serviceapi.productinformation.awards;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Award {

@SerializedName("awardCode")
@Expose
private String awardCode;
@SerializedName("awardName")
@Expose
private String awardName;
@SerializedName("awardDate")
@Expose
private String awardDate;
@SerializedName("awardPlace")
@Expose
private String awardPlace;
@SerializedName("awardDescription")
@Expose
private String awardDescription;
@SerializedName("awardAcknowledgement")
@Expose
private String awardAcknowledgement;
@SerializedName("awardRank")
@Expose
private String awardRank;
@SerializedName("awardType")
@Expose
private String awardType;
@SerializedName("assets")
@Expose
private List<Asset> assets = new ArrayList<Asset>();
@SerializedName("awardText")
@Expose
private String awardText;
@SerializedName("title")
@Expose
private String title;
@SerializedName("rating")
@Expose
private String rating;
@SerializedName("awardAuthor")
@Expose
private String awardAuthor;
@SerializedName("testPros")
@Expose
private String testPros;
@SerializedName("testCons")
@Expose
private String testCons;
@SerializedName("awardVerdict")
@Expose
private String awardVerdict;
@SerializedName("awardSourceCode")
@Expose
private String awardSourceCode;
@SerializedName("awardCategory")
@Expose
private String awardCategory;
@SerializedName("awardSourceLocale")
@Expose
private String awardSourceLocale;
@SerializedName("awardAlid")
@Expose
private String awardAlid;
@SerializedName("globalSource")
@Expose
private String globalSource;
@SerializedName("status")
@Expose
private String status;

/**
* No args constructor for use in serialization
* 
*/
public Award() {
}

/**
* 
* @param awardAcknowledgement
* @param awardSourceLocale
* @param status
* @param awardPlace
* @param awardDescription
* @param awardDate
* @param awardText
* @param awardCategory
* @param awardCode
* @param awardAlid
* @param awardType
* @param assets
* @param testPros
* @param title
* @param globalSource
* @param testCons
* @param awardAuthor
* @param awardSourceCode
* @param awardVerdict
* @param awardRank
* @param rating
* @param awardName
*/
public Award(String awardCode, String awardName, String awardDate, String awardPlace, String awardDescription, String awardAcknowledgement, String awardRank, String awardType, List<Asset> assets, String awardText, String title, String rating, String awardAuthor, String testPros, String testCons, String awardVerdict, String awardSourceCode, String awardCategory, String awardSourceLocale, String awardAlid, String globalSource, String status) {
this.awardCode = awardCode;
this.awardName = awardName;
this.awardDate = awardDate;
this.awardPlace = awardPlace;
this.awardDescription = awardDescription;
this.awardAcknowledgement = awardAcknowledgement;
this.awardRank = awardRank;
this.awardType = awardType;
this.assets = assets;
this.awardText = awardText;
this.title = title;
this.rating = rating;
this.awardAuthor = awardAuthor;
this.testPros = testPros;
this.testCons = testCons;
this.awardVerdict = awardVerdict;
this.awardSourceCode = awardSourceCode;
this.awardCategory = awardCategory;
this.awardSourceLocale = awardSourceLocale;
this.awardAlid = awardAlid;
this.globalSource = globalSource;
this.status = status;
}

/**
* 
* @return
* The awardCode
*/
public String getAwardCode() {
return awardCode;
}

/**
* 
* @param awardCode
* The awardCode
*/
public void setAwardCode(String awardCode) {
this.awardCode = awardCode;
}

public Award withAwardCode(String awardCode) {
this.awardCode = awardCode;
return this;
}

/**
* 
* @return
* The awardName
*/
public String getAwardName() {
return awardName;
}

/**
* 
* @param awardName
* The awardName
*/
public void setAwardName(String awardName) {
this.awardName = awardName;
}

public Award withAwardName(String awardName) {
this.awardName = awardName;
return this;
}

/**
* 
* @return
* The awardDate
*/
public String getAwardDate() {
return awardDate;
}

/**
* 
* @param awardDate
* The awardDate
*/
public void setAwardDate(String awardDate) {
this.awardDate = awardDate;
}

public Award withAwardDate(String awardDate) {
this.awardDate = awardDate;
return this;
}

/**
* 
* @return
* The awardPlace
*/
public String getAwardPlace() {
return awardPlace;
}

/**
* 
* @param awardPlace
* The awardPlace
*/
public void setAwardPlace(String awardPlace) {
this.awardPlace = awardPlace;
}

public Award withAwardPlace(String awardPlace) {
this.awardPlace = awardPlace;
return this;
}

/**
* 
* @return
* The awardDescription
*/
public String getAwardDescription() {
return awardDescription;
}

/**
* 
* @param awardDescription
* The awardDescription
*/
public void setAwardDescription(String awardDescription) {
this.awardDescription = awardDescription;
}

public Award withAwardDescription(String awardDescription) {
this.awardDescription = awardDescription;
return this;
}

/**
* 
* @return
* The awardAcknowledgement
*/
public String getAwardAcknowledgement() {
return awardAcknowledgement;
}

/**
* 
* @param awardAcknowledgement
* The awardAcknowledgement
*/
public void setAwardAcknowledgement(String awardAcknowledgement) {
this.awardAcknowledgement = awardAcknowledgement;
}

public Award withAwardAcknowledgement(String awardAcknowledgement) {
this.awardAcknowledgement = awardAcknowledgement;
return this;
}

/**
* 
* @return
* The awardRank
*/
public String getAwardRank() {
return awardRank;
}

/**
* 
* @param awardRank
* The awardRank
*/
public void setAwardRank(String awardRank) {
this.awardRank = awardRank;
}

public Award withAwardRank(String awardRank) {
this.awardRank = awardRank;
return this;
}

/**
* 
* @return
* The awardType
*/
public String getAwardType() {
return awardType;
}

/**
* 
* @param awardType
* The awardType
*/
public void setAwardType(String awardType) {
this.awardType = awardType;
}

public Award withAwardType(String awardType) {
this.awardType = awardType;
return this;
}

/**
* 
* @return
* The assets
*/
public List<Asset> getAssets() {
return assets;
}

/**
* 
* @param assets
* The assets
*/
public void setAssets(List<Asset> assets) {
this.assets = assets;
}

public Award withAssets(List<Asset> assets) {
this.assets = assets;
return this;
}

/**
* 
* @return
* The awardText
*/
public String getAwardText() {
return awardText;
}

/**
* 
* @param awardText
* The awardText
*/
public void setAwardText(String awardText) {
this.awardText = awardText;
}

public Award withAwardText(String awardText) {
this.awardText = awardText;
return this;
}

/**
* 
* @return
* The title
*/
public String getTitle() {
return title;
}

/**
* 
* @param title
* The title
*/
public void setTitle(String title) {
this.title = title;
}

public Award withTitle(String title) {
this.title = title;
return this;
}

/**
* 
* @return
* The rating
*/
public String getRating() {
return rating;
}

/**
* 
* @param rating
* The rating
*/
public void setRating(String rating) {
this.rating = rating;
}

public Award withRating(String rating) {
this.rating = rating;
return this;
}

/**
* 
* @return
* The awardAuthor
*/
public String getAwardAuthor() {
return awardAuthor;
}

/**
* 
* @param awardAuthor
* The awardAuthor
*/
public void setAwardAuthor(String awardAuthor) {
this.awardAuthor = awardAuthor;
}

public Award withAwardAuthor(String awardAuthor) {
this.awardAuthor = awardAuthor;
return this;
}

/**
* 
* @return
* The testPros
*/
public String getTestPros() {
return testPros;
}

/**
* 
* @param testPros
* The testPros
*/
public void setTestPros(String testPros) {
this.testPros = testPros;
}

public Award withTestPros(String testPros) {
this.testPros = testPros;
return this;
}

/**
* 
* @return
* The testCons
*/
public String getTestCons() {
return testCons;
}

/**
* 
* @param testCons
* The testCons
*/
public void setTestCons(String testCons) {
this.testCons = testCons;
}

public Award withTestCons(String testCons) {
this.testCons = testCons;
return this;
}

/**
* 
* @return
* The awardVerdict
*/
public String getAwardVerdict() {
return awardVerdict;
}

/**
* 
* @param awardVerdict
* The awardVerdict
*/
public void setAwardVerdict(String awardVerdict) {
this.awardVerdict = awardVerdict;
}

public Award withAwardVerdict(String awardVerdict) {
this.awardVerdict = awardVerdict;
return this;
}

/**
* 
* @return
* The awardSourceCode
*/
public String getAwardSourceCode() {
return awardSourceCode;
}

/**
* 
* @param awardSourceCode
* The awardSourceCode
*/
public void setAwardSourceCode(String awardSourceCode) {
this.awardSourceCode = awardSourceCode;
}

public Award withAwardSourceCode(String awardSourceCode) {
this.awardSourceCode = awardSourceCode;
return this;
}

/**
* 
* @return
* The awardCategory
*/
public String getAwardCategory() {
return awardCategory;
}

/**
* 
* @param awardCategory
* The awardCategory
*/
public void setAwardCategory(String awardCategory) {
this.awardCategory = awardCategory;
}

public Award withAwardCategory(String awardCategory) {
this.awardCategory = awardCategory;
return this;
}

/**
* 
* @return
* The awardSourceLocale
*/
public String getAwardSourceLocale() {
return awardSourceLocale;
}

/**
* 
* @param awardSourceLocale
* The awardSourceLocale
*/
public void setAwardSourceLocale(String awardSourceLocale) {
this.awardSourceLocale = awardSourceLocale;
}

public Award withAwardSourceLocale(String awardSourceLocale) {
this.awardSourceLocale = awardSourceLocale;
return this;
}

/**
* 
* @return
* The awardAlid
*/
public String getAwardAlid() {
return awardAlid;
}

/**
* 
* @param awardAlid
* The awardAlid
*/
public void setAwardAlid(String awardAlid) {
this.awardAlid = awardAlid;
}

public Award withAwardAlid(String awardAlid) {
this.awardAlid = awardAlid;
return this;
}

/**
* 
* @return
* The globalSource
*/
public String getGlobalSource() {
return globalSource;
}

/**
* 
* @param globalSource
* The globalSource
*/
public void setGlobalSource(String globalSource) {
this.globalSource = globalSource;
}

public Award withGlobalSource(String globalSource) {
this.globalSource = globalSource;
return this;
}

/**
* 
* @return
* The status
*/
public String getStatus() {
return status;
}

/**
* 
* @param status
* The status
*/
public void setStatus(String status) {
this.status = status;
}

public Award withStatus(String status) {
this.status = status;
return this;
}

}

