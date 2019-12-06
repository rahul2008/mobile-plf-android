package com.philips.cdp.registration.controller;


import androidx.annotation.VisibleForTesting;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import org.json.JSONObject;

import javax.inject.Inject;

public class RussianConsent {

    private static final String JANRAIN_FILED_ONE = "one";
    private static final String JANRAIN_FILED_CONTROL_FIELDS = "controlFields";
    private static final String RUSSIAN_COUNTRY_CODE = "RU";
    private static final String JANRAIN_FIELD_JANRAIN = "janrain";
    @Inject
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    RussianConsent() {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
    }

    JSONObject addRussianConsent(JSONObject newUser) {
        String currentCountry = serviceDiscoveryInterface.getHomeCountry();
        if (currentCountry.equalsIgnoreCase(RUSSIAN_COUNTRY_CODE)) {
            try {
                JSONObject one = new JSONObject().put(JANRAIN_FILED_ONE, "true");
                JSONObject control = new JSONObject().put(JANRAIN_FILED_CONTROL_FIELDS, one);
                newUser.put(JANRAIN_FIELD_JANRAIN, control);
                return newUser;
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @VisibleForTesting
    void injectMocks(ServiceDiscoveryInterface mockServiceDiscoveryInterface) {
        this.serviceDiscoveryInterface = mockServiceDiscoveryInterface;
    }
}
