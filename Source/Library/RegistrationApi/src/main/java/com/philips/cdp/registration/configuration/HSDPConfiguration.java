/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;

import android.support.annotation.Nullable;

import com.philips.cdp.registration.ui.utils.RegUtility;

import java.util.HashMap;


public class HSDPConfiguration {


    private HashMap<Configuration, HSDPInfo> hsdpInfos = new HashMap<>();

    /**
     * Get All HSDP information with all configuration
     *
     * @return Map of Configuration and its informantion.
     */
    public HashMap<Configuration, HSDPInfo> getHsdpInfos() {
        return hsdpInfos;
    }

    /**
     * Set HSDP Informations for different Configurations
     *
     * @param hsdpInfos Map of Configuration and its informantion.
     */
    public void setHsdpInfos(HashMap<Configuration, HSDPInfo> hsdpInfos) {
        this.hsdpInfos = hsdpInfos;
    }

    /**
     * Get HSDP information for specified configuration
     *
     * @param configuration
     * @return HSDPInfo Object
     */
    public HSDPInfo getHSDPInfo(Configuration configuration) {
        return hsdpInfos.get(configuration);
    }

    /**
     * Set HSDP Information for specific Configuraton
     *
     * @param configuration
     * @param hsdpInfo      HSDP Information
     */
    public void setHSDPInfo(Configuration configuration, HSDPInfo hsdpInfo) {
        hsdpInfos.put(configuration, hsdpInfo);
    }


    public boolean isHsdpFlow() {
        HSDPConfiguration hsdpConfiguration = RegistrationConfiguration.getInstance().getHsdpConfiguration();
        if (hsdpConfiguration == null) {
            return false;
        }

        if (hsdpConfiguration.getHsdpInfos().size() == 0) {
            return false;
        }
        String environment = RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment();
        if (environment == null) {
            return false;
        }

        HSDPInfo hsdpInfo = hsdpConfiguration.getHSDPInfo(RegUtility.getConfiguration(environment));
        if (hsdpInfo == null) {
            throw new RuntimeException("HSDP configuration is not configured for " + environment + " environment ");
        }
        if (null != hsdpConfiguration && null != hsdpInfo) {

            String exception = buildException(hsdpInfo);

            if (null != exception) {
                throw new RuntimeException("HSDP configuration is not configured for " + RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment() + " environment for " + exception.toString().substring(4));
            }
        }


        return (null != hsdpInfo.getApplicationName() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getSecreteId()
                && null != hsdpInfo.getBaseURL());
    }

    private String buildException(HSDPInfo hsdpInfo) {
        String exception = null;

        if (hsdpInfo.getApplicationName() == null) {
            exception += "Application Name";
        }

        if (hsdpInfo.getSharedId() == null) {
            if (null != exception) {
                exception += ",shared key ";
            } else {
                exception += "shared key ";
            }
        }
        if (hsdpInfo.getSecreteId() == null) {
            if (null != exception) {
                exception += ",Secret key ";
            } else {
                exception += "Secret key ";
            }
        }

        if (hsdpInfo.getBaseURL() == null) {
            if (null != exception) {
                exception += ",Base Url ";
            } else {
                exception += "Base Url ";
            }
        }
        return exception;
    }
}
