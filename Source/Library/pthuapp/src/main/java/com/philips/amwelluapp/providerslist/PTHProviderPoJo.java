package com.philips.amwelluapp.providerslist;

/**
 * Created by philips on 6/19/17.
 */

public class PTHProviderPoJo {

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
