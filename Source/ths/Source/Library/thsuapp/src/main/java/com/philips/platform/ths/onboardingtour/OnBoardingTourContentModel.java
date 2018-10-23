package com.philips.platform.ths.onboardingtour;

class OnBoardingTourContentModel {

    private int tourPageTextId;
    private int tourBackgroundDrawable;
    private boolean isAmwellLogoVisible;

    private int title;

    String getPageTitle() {
        return pageTitle;
    }

    private final String pageTitle;


    OnBoardingTourContentModel(int tourPageTextId, int tourBackgroundDrawable, String pageTitle, int title) {
        this.tourPageTextId = tourPageTextId;
        this.tourBackgroundDrawable = tourBackgroundDrawable;
        this.pageTitle = pageTitle;
        this.title = title;
        this.setIsAmwellLogoVisible(false);
    }

    int getTourPageTextId() {
        return tourPageTextId;
    }

    int getTourBackgroundDrawable() {
        return tourBackgroundDrawable;
    }

    boolean getIsAmwellLogoVisible() {
        return isAmwellLogoVisible;
    }

    void setIsAmwellLogoVisible(boolean isAmwellLogoVisible) {
        this.isAmwellLogoVisible = isAmwellLogoVisible;
    }

    int getTitleId() {
        return title;
    }
}
