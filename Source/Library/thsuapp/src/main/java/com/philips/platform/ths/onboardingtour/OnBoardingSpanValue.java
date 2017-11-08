package com.philips.platform.ths.onboardingtour;

import java.io.Serializable;

class OnBoardingSpanValue implements Serializable{

    private int startIndex;
    private int endIndex;
    private OnBoardingTypeface onBoardingTypeface;
    enum OnBoardingTypeface {
        BOLD,
        BOOK
    }

    OnBoardingSpanValue(int startIndex, int endIndex, OnBoardingTypeface onBoardingTypeface) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.onBoardingTypeface = onBoardingTypeface;
    }

    int getStartIndex() {
        return startIndex;
    }

    int getEndIndex() {
        return endIndex;
    }

    OnBoardingTypeface getOnBoardingTypeface() {
        return onBoardingTypeface;
    }
}
