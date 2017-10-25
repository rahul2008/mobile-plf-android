package com.philips.platform.ths.onboardingtour;

/**
 * Created by philips on 10/25/17.
 */

public class OnBoardingTourContentModel {

    private int tourTitleId;
    private int tourBackgroundDrawable;

    public int getTourPageTitleId() {
        return tourPageTitleId;
    }

    public void setTourPageTitleId(int tourPageTitleId) {
        this.tourPageTitleId = tourPageTitleId;
    }

    private int tourPageTitleId;

    public OnBoardingTourContentModel(int tourTitleId, int tourBackgroundDrawable, int tourPageTitleId) {
        this.tourTitleId = tourTitleId;
        this.tourBackgroundDrawable = tourBackgroundDrawable;
        this.tourPageTitleId = tourPageTitleId;
    }


    public int getTourTitleId() {
        return tourTitleId;
    }

    public void setTourTitleId(int tourTitleId) {
        this.tourTitleId = tourTitleId;
    }

    public int getTourBackgroundDrawable() {
        return tourBackgroundDrawable;
    }

    public void setTourBackgroundDrawable(int tourBackgroundDrawable) {
        this.tourBackgroundDrawable = tourBackgroundDrawable;
    }

}
