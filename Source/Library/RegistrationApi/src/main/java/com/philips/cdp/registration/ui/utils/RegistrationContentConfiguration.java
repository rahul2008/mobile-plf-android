package com.philips.cdp.registration.ui.utils;

import java.io.Serializable;

/**
 * Created by 310243576 on 4/12/2017.
 */

public class RegistrationContentConfiguration implements Serializable {

    private String valueForRegistration;

    private String valueForEmailVerification;

    private String optInTitleText;

    private String optInQuessionaryText;

    private String optInDetailDescription;

    private String optInBannerText;

    public String getOptInActionBarText() {
        return optInActionBarText;
    }

    public void setOptInActionBarText(String optInActionBarText) {
        this.optInActionBarText = optInActionBarText;
    }

    private String optInActionBarText;


    public String getValueForRegistration() {
        return valueForRegistration;
    }

    public void setValueForRegistration(String valueForRegistration) {
        this.valueForRegistration = valueForRegistration;
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

}
