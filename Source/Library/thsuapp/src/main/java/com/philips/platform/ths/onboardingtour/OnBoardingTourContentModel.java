package com.philips.platform.ths.onboardingtour;

/**
 * Created by philips on 10/25/17.
 */

public class OnBoardingTourContentModel {

    private int tourPageTextId;
    private int tourBackgroundDrawable;


    public OnBoardingTourContentModel(int tourPageTextId, int tourBackgroundDrawable) {
        this.tourPageTextId = tourPageTextId;
        this.tourBackgroundDrawable = tourBackgroundDrawable;
    }


    public int getTourPageTextId() {
        return tourPageTextId;
    }

    public void setTourPageTextId(int tourPageTextId) {
        this.tourPageTextId = tourPageTextId;
    }

    public int getTourBackgroundDrawable() {
        return tourBackgroundDrawable;
    }

    public void setTourBackgroundDrawable(int tourBackgroundDrawable) {
        this.tourBackgroundDrawable = tourBackgroundDrawable;
    }

}
