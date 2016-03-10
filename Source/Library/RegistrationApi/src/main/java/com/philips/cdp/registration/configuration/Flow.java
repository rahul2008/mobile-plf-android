
package com.philips.cdp.registration.configuration;

import java.util.HashMap;

public class Flow {

    private Boolean emailVerificationRequired = true;

    private Boolean termsAndConditionsAcceptanceRequired;

    private HashMap<String, String> minAgeLimit;

    /**
     * Status of email verification required
     *
     * @return boolean
     */
    public Boolean isEmailVerificationRequired() {
        return emailVerificationRequired;
    }

    /**
     * Set Email verification status
     *
     * @param emailVerificationRequired
     */
    public void setEmailVerificationRequired(Boolean emailVerificationRequired) {
        this.emailVerificationRequired = emailVerificationRequired;
    }

    /**
     * Status of terms and condition accepatance
     *
     * @return boolean
     */
    public Boolean isTermsAndConditionsAcceptanceRequired() {
        return termsAndConditionsAcceptanceRequired;
    }

    /**
     * Set terms and condition acceptance required or no
     *
     * @param termsAndConditionsAcceptanceRequired
     */
    public void setTermsAndConditionsAcceptanceRequired(Boolean termsAndConditionsAcceptanceRequired) {
        this.termsAndConditionsAcceptanceRequired = termsAndConditionsAcceptanceRequired;
    }

    /**
     * Get min age limit map
     *
     * @return Map of Country and Age restrictions
     */
    public HashMap<String, String> getMinAgeLimit() {
        return minAgeLimit;
    }

    /**
     * Set Min age Limit
     *
     * @param minAgeLimit Map with Key value pair of country code and min age
     */

    public void setMinAgeLimit(HashMap<String, String> minAgeLimit) {
        this.minAgeLimit = minAgeLimit;
    }

    /**
     * Get minimium age for country
     *
     * @param countryCode Country code
     * @return integer value of min age if mapping available else 0
     */
    public int getMinAgeLimitByCountry(String countryCode) {
        String DEFAULT = "default";
        if (null != minAgeLimit) {
            if (minAgeLimit.containsKey(countryCode)) {
                return Integer.parseInt(minAgeLimit.get(countryCode.toUpperCase()));
            } else {
                return Integer.parseInt(minAgeLimit.get(DEFAULT));
            }
        }
        return 0;
    }
}
