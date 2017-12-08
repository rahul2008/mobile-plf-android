/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerslist;

public class THSProviderPoJo {

    private String providerName;
    private String providerPractise;
    private boolean isProviderAvailable;
    private float providerRating;
    private String providerImageURL;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderPractise() {
        return providerPractise;
    }

    public void setProviderPractise(String providerPractise) {
        this.providerPractise = providerPractise;
    }

    public boolean isProviderAvailable() {
        return isProviderAvailable;
    }

    public void setProviderAvailable(boolean providerAvailable) {
        isProviderAvailable = providerAvailable;
    }

    public float getProviderRating() {
        return providerRating;
    }

    public void setProviderRating(float providerRating) {
        this.providerRating = providerRating;
    }

    public String getProviderImageURL() {
        return providerImageURL;
    }

    public void setProviderImageURL(String providerImageURL) {
        this.providerImageURL = providerImageURL;
    }

}
