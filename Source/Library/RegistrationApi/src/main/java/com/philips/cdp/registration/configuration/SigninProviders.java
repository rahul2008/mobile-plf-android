
package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class SigninProviders {

    private final String DEFAULT = "default";

    private HashMap<String, ArrayList<String>> providers;

    public HashMap<String, ArrayList<String>> getProviders() {
        return providers;
    }

    /**
     * Set providers in Hash map first param is country code and other is enabled providers list in Hash map
     *
     * @param providers
     */
    public void setProviders(HashMap<String, ArrayList<String>> providers) {

        RegUtility.checkIsValidSignInProviders(providers);
        this.providers = providers;
    }

    /**
     * Get provoders
     *
     * @param countryCode Country code
     * @return List of providers
     */
    public ArrayList<String> getProvidersForCountry(String countryCode) {
        ArrayList<String> signinProviders = null;
        if (providers != null) {
            signinProviders = providers.get(countryCode.toUpperCase());
            if (null == signinProviders) {
                signinProviders = providers.get(DEFAULT.toUpperCase());
            }
        }
        return signinProviders;
    }

}
