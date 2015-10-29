package com.philips.cdp.serviceapi.productinformation.overview;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ReviewStatistics {

@SerializedName("averageOverallRating")
@Expose
private double averageOverallRating;
@SerializedName("totalReviewCount")
@Expose
private long totalReviewCount;

/**
* No args constructor for use in serialization
* 
*/
public ReviewStatistics() {
}

/**
* 
* @param averageOverallRating
* @param totalReviewCount
*/
public ReviewStatistics(double averageOverallRating, long totalReviewCount) {
this.averageOverallRating = averageOverallRating;
this.totalReviewCount = totalReviewCount;
}

/**
* 
* @return
* The averageOverallRating
*/
public double getAverageOverallRating() {
return averageOverallRating;
}

/**
* 
* @param averageOverallRating
* The averageOverallRating
*/
public void setAverageOverallRating(double averageOverallRating) {
this.averageOverallRating = averageOverallRating;
}

public ReviewStatistics withAverageOverallRating(double averageOverallRating) {
this.averageOverallRating = averageOverallRating;
return this;
}

/**
* 
* @return
* The totalReviewCount
*/
public long getTotalReviewCount() {
return totalReviewCount;
}

/**
* 
* @param totalReviewCount
* The totalReviewCount
*/
public void setTotalReviewCount(long totalReviewCount) {
this.totalReviewCount = totalReviewCount;
}

public ReviewStatistics withTotalReviewCount(long totalReviewCount) {
this.totalReviewCount = totalReviewCount;
return this;
}

}