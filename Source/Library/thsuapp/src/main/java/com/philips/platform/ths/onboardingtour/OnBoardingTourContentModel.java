package com.philips.platform.ths.onboardingtour;

import java.util.List;

class OnBoardingTourContentModel {

    private int tourPageTextId;
    private int tourBackgroundDrawable;
    private List<OnBoardingSpanValue> spanValues;


    OnBoardingTourContentModel(int tourPageTextId, int tourBackgroundDrawable, List<OnBoardingSpanValue> spanValues) {
        this.tourPageTextId = tourPageTextId;
        this.tourBackgroundDrawable = tourBackgroundDrawable;
        this.spanValues = spanValues;
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
