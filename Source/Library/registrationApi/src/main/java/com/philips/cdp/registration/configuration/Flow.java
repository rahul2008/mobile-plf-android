
package com.philips.cdp.registration.configuration;

import java.util.HashMap;

public class Flow {

    private Boolean emailVerificationRequired;

    private Boolean termsAndConditionsAcceptanceRequired;

    private HashMap<String, String> minAgeLimit;

    public Boolean isEmailVerificationRequired() {
        return emailVerificationRequired;
    }

    public void setEmailVerificationRequired(Boolean emailVerificationRequired) {
        this.emailVerificationRequired = emailVerificationRequired;
    }

    public Boolean isTermsAndConditionsAcceptanceRequired() {
        return termsAndConditionsAcceptanceRequired;
    }

    public void setTermsAndConditionsAcceptanceRequired(Boolean termsAndConditionsAcceptanceRequired) {
        this.termsAndConditionsAcceptanceRequired = termsAndConditionsAcceptanceRequired;
    }

    public HashMap<String, String> getMinAgeLimit() {
        return minAgeLimit;
    }

    public void setMinAgeLimit(HashMap<String, String> minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    public int getMinAgeLimitByCountry(String countryCode) {
        String DEFAULT = "default";
        if(null!=minAgeLimit){
            if(minAgeLimit.containsKey(countryCode)){
                return Integer.parseInt(minAgeLimit.get(countryCode.toUpperCase()));
            }else{
                return Integer.parseInt(minAgeLimit.get(DEFAULT));
            }
        }
        return 0;
    }
}
