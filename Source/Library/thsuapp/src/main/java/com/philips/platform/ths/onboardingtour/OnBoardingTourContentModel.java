package com.philips.platform.ths.onboardingtour;

import java.util.List;

class OnBoardingTourContentModel {

    private int tourPageTextId;
    private int tourBackgroundDrawable;
    private List<OnBoardingSpanValue> spanValues;

    public String getPageTitle() {
        return pageTitle;
    }

    private final String pageTitle;


    OnBoardingTourContentModel(int tourPageTextId, int tourBackgroundDrawable, List<OnBoardingSpanValue> spanValues, String pageTitle) {
        this.tourPageTextId = tourPageTextId;
        this.tourBackgroundDrawable = tourBackgroundDrawable;
        this.spanValues = spanValues;
        this.pageTitle = pageTitle;
    }

    List<OnBoardingSpanValue> getSpanValues() {
        return spanValues;
    }

    int getTourPageTextId() {
        return tourPageTextId;
    }

    public void setTourPageTextId(int tourPageTextId) {
        this.tourPageTextId = tourPageTextId;
    }

    int getTourBackgroundDrawable() {
        return tourBackgroundDrawable;
    }

    public void setTourBackgroundDrawable(int tourBackgroundDrawable) {
        this.tourBackgroundDrawable = tourBackgroundDrawable;
    }

}
