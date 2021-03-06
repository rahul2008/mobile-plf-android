/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.model.summary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReviewStatistics implements Serializable {

    private static final long serialVersionUID = -3050137840961928837L;
    private float averageOverallRating;
    private int totalReviewCount;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The averageOverallRating
     */
    public float getAverageOverallRating() {
        return averageOverallRating;
    }

    /**
     * @param averageOverallRating The averageOverallRating
     */
    public void setAverageOverallRating(float averageOverallRating) {
        this.averageOverallRating = averageOverallRating;
    }

    /**
     * @return The totalReviewCount
     */
    public int getTotalReviewCount() {
        return totalReviewCount;
    }

    /**
     * @param totalReviewCount The totalReviewCount
     */
    public void setTotalReviewCount(int totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
