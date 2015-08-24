
package com.philips.cdp.registration.configuration;

import java.util.HashMap;
import java.util.Locale;

public class Flow {

    private boolean emailVerificationRequired;

    private boolean termsAndConditionsAcceptanceRequired;

    private HashMap<String, String> minAgeLimit;

    public boolean isEmailVerificationRequired() {
        return emailVerificationRequired;
    }

    public void setEmailVerificationRequired(boolean emailVerificationRequired) {
        this.emailVerificationRequired = emailVerificationRequired;
    }

    public boolean isTermsAndConditionsAcceptanceRequired() {
        return termsAndConditionsAcceptanceRequired;
    }

    public void setTermsAndConditionsAcceptanceRequired(boolean termsAndConditionsAcceptanceRequired) {
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
                return Integer.parseInt(minAgeLimit.get(countryCode.toUpperCase(Locale.getDefault())));
            }else{
                return Integer.parseInt(minAgeLimit.get(DEFAULT));
            }
        }
        return 0;
    }
}
