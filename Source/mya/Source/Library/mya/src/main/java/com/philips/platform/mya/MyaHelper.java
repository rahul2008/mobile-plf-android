/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.mya.catk.CatkInputs;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MyaHelper {

    private static MyaHelper instance;
    private AppInfraInterface appInfra;
    private MyaListener myaListener;
    private ThemeConfiguration themeConfiguration;
    private MyaLaunchInput myaLaunchInput;
    private List<ConsentConfiguration> consentConfigurationList;
    private LoggingInterface myaLogger;

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

    public void setConfigurations(List<ConsentConfiguration> consentConfigurationList) {
        throwExceptionWhenDuplicateTypesExist(consentConfigurationList);
        this.consentConfigurationList = consentConfigurationList == null ? new ArrayList<ConsentConfiguration>() : consentConfigurationList;
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

    public MyaLaunchInput getMyaLaunchInput() {
        return myaLaunchInput;
    }

    public void setMyaLaunchInput(MyaLaunchInput myaLaunchInput) {
        this.myaLaunchInput = myaLaunchInput;
    }

    public void setMyaLogger(LoggingInterface myaLogger) {
        this.myaLogger = myaLogger;
    }

    public LoggingInterface getMyaLogger() {
        return myaLogger;
    }
}
