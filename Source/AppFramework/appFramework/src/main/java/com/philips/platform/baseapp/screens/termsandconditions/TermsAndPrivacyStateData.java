package com.philips.platform.baseapp.screens.termsandconditions;

import com.philips.platform.appframework.flowmanager.base.UIStateData;

/**
 * Created by philips on 31/07/17.
 */

public class TermsAndPrivacyStateData extends UIStateData {

    private TermsAndPrivacyEnum termsAndPrivacyEnum;

    public TermsAndPrivacyEnum getTermsAndPrivacyEnum() {
        return termsAndPrivacyEnum;
    }

    public void setTermsAndPrivacyEnum(TermsAndPrivacyEnum termsAndPrivacyEnum) {
        this.termsAndPrivacyEnum = termsAndPrivacyEnum;
    }

    public enum TermsAndPrivacyEnum{
        TERMS_CLICKED,PRIVACY_CLICKED
    }

}
