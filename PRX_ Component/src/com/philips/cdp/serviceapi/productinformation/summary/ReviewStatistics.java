package com.philips.cdp.serviceapi.productinformation.summary;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ReviewStatistics {

@SerializedName("averageOverallRating")
@Expose
private Double averageOverallRating;
@SerializedName("totalReviewCount")
@Expose
private Integer totalReviewCount;

/**
* 
* @return
* The averageOverallRating
*/
public Double getAverageOverallRating() {
return averageOverallRating;
}

/**
* 
* @param averageOverallRating
* The averageOverallRating
*/
public void setAverageOverallRating(Double averageOverallRating) {
this.averageOverallRating = averageOverallRating;
}

/**
* 
* @return
* The totalReviewCount
*/
public Integer getTotalReviewCount() {
return totalReviewCount;
}

/**
* 
* @param totalReviewCount
* The totalReviewCount
*/
public void setTotalReviewCount(Integer totalReviewCount) {
this.totalReviewCount = totalReviewCount;
}

}
