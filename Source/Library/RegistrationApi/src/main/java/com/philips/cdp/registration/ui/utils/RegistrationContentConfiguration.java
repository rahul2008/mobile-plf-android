package com.philips.cdp.registration.ui.utils;

import java.io.*;


public class RegistrationContentConfiguration implements Serializable {

    private String valueForRegistrationTitle;

    private String valueForEmailVerification;

    private String valueForRegistrationDescription;

    private String optInTitleText;

    private String optInQuessionaryText;

    private String optInDetailDescription;

    private String optInBannerText;

    private boolean enableLastName;

    private boolean enableContinueWithouAccount;

    public String getValueForRegistrationDescription() {
        return valueForRegistrationDescription;
    }

    public void setValueForRegistrationDescription(String valueForRegistrationDescription) {
        this.valueForRegistrationDescription = valueForRegistrationDescription;
    }

    public String getOptInActionBarText() {
        return optInActionBarText;
    }

    public void setOptInActionBarText(String optInActionBarText) {
        this.optInActionBarText = optInActionBarText;
    }

    private String optInActionBarText;


    public String getValueForRegistrationTitle() {
        return valueForRegistrationTitle;
    }

    public void setValueForRegistrationTitle(String valueForRegistrationTitle) {
        this.valueForRegistrationTitle = valueForRegistrationTitle;
    }

    public String getValueForEmailVerification() {
        return valueForEmailVerification;
    }

    public void setValueForEmailVerification(String valueForEmailVerification) {
        this.valueForEmailVerification = valueForEmailVerification;
    }

    public String getOptInTitleText() {
        return optInTitleText;
    }

    public void setOptInTitleText(String optInTitleText) {
        this.optInTitleText = optInTitleText;
    }

    public String getOptInQuessionaryText() {
        return optInQuessionaryText;
    }

    public void setOptInQuessionaryText(String optInQuessionaryText) {
        this.optInQuessionaryText = optInQuessionaryText;
    }

    public String getOptInDetailDescription() {
        return optInDetailDescription;
    }

    public void setOptInDetailDescription(String optInDetailDescription) {
        this.optInDetailDescription = optInDetailDescription;
    }

    public String getOptInBannerText() {
        return optInBannerText;
    }

    public void setOptInBannerText(String optInBannerText) {
        this.optInBannerText = optInBannerText;
    }


    public boolean getEnableLastName() {
        return enableLastName;
    }

    public void enableLastName(boolean hideCreateAccountLastName) {
        this.enableLastName = hideCreateAccountLastName;
    }

    public boolean getEnableContinueWithouAccount() {
        return enableContinueWithouAccount;
    }

    public void enableContinueWithouAccount(boolean hideHomeScreenContinueWithouAccount) {
        this.enableContinueWithouAccount = hideHomeScreenContinueWithouAccount;
    }

}
