/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;


import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.registration.app.infra.AppInfraWrapper;
import com.philips.cdp.registration.settings.RegistrationHelper;
import java.util.Map;

import javax.inject.Inject;

public class BaseConfiguration {

    private static final String DEFAULT_PROPERTY_KEY = "default";

    @Inject
    protected AppInfraWrapper appInfraWrapper;

    public BaseConfiguration() {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
    }

    @Nullable
    protected String getConfigPropertyValue(Object property) {
        if(property == null) {
            return null;
        }
        if (property instanceof String) {
            return (String) property;
        }
        if (property instanceof Map) {
            return getPropertyValueFromMap((Map) property);
        }
        return null;
    }

    private String getPropertyValueFromMap(Map property) {
        String propertyValue = (String) property.get(RegistrationHelper.getInstance().getCountryCode());
        if (propertyValue == null || propertyValue.isEmpty()) {
            propertyValue = (String) property.get(DEFAULT_PROPERTY_KEY);
        }
        return propertyValue;
    }

    @VisibleForTesting
    protected void setAppInfraWrapper(AppInfraWrapper appInfraWrapper) {
        this.appInfraWrapper = appInfraWrapper;
    }
}
