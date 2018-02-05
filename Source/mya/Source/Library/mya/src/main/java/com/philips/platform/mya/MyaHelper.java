/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import java.util.ArrayList;
import java.util.List;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.catk.CatkInputs;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class MyaHelper {

    private static MyaHelper instance;
    private AppInfraInterface appInfra;
    private MyaListener myaListener;
    private ThemeConfiguration themeConfiguration;
    private List<ConsentConfiguration> consentConfigurationList;
    private String privacyNoticeUrl;

    private MyaHelper() {
    }

    public static MyaHelper getInstance() {
        if (instance == null) {
            synchronized (MyaHelper.class) {
                if (instance == null) {
                    instance = new MyaHelper();
                }
            }
        }
        return instance;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public void setAppInfra(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    public List<ConsentConfiguration> getConsentConfigurationList() {
        return consentConfigurationList;
    }

    public String getPrivacyNoticeUrl() {
        return privacyNoticeUrl;
    }

    public void setConfigurations(List<ConsentConfiguration> consentConfigurationList) {
        throwExceptionWhenDuplicateTypesExist(consentConfigurationList);
        this.consentConfigurationList = consentConfigurationList == null ? new ArrayList<ConsentConfiguration>() : consentConfigurationList;
    }

    public void setPrivacyNoticeUrl(String privacyNoticeUrl) {
        this.privacyNoticeUrl = privacyNoticeUrl;
    }

    private void throwExceptionWhenDuplicateTypesExist(List<ConsentConfiguration> consentConfigurationList) {
        List<String> uniqueTypes = new ArrayList<>();
        if (consentConfigurationList != null && !consentConfigurationList.isEmpty()) {
            for (ConsentConfiguration configuration : consentConfigurationList) {
                if (configuration != null) {
                    for (ConsentDefinition definition : configuration.getConsentDefinitionList()) {
                        if (definition != null) {
                            for (String type : definition.getTypes()) {
                                if (uniqueTypes.contains(type)) {
                                    throw new CatkInputs.InvalidInputException(
                                            "Not allowed to have duplicate types in your Definitions, type:" + type + " occurs in multiple times");
                                }
                                uniqueTypes.add(type);
                            }
                        }
                    }
                }
            }
        }
    }

    public MyaListener getMyaListener() {
        return myaListener;
    }

    public void setMyaListener(MyaListener myaListener) {
        this.myaListener = myaListener;
    }

    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public void setThemeConfiguration(ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }
}
