/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.ConsentRegistryInterface;
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
    private List<ConsentDefinition> consentDefinitionList;
    private ConsentRegistryInterface consentRegistryInterface;

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

    public ConsentRegistryInterface getConsentRegistryInterface() {
        return consentRegistryInterface;
    }

    public void setConsentRegistryInterface(ConsentRegistryInterface consentRegistryInterface) {
        this.consentRegistryInterface = consentRegistryInterface;
    }

    public List<ConsentDefinition> getConsentDefinitionList() {
        return consentDefinitionList;
    }

    public void setConsentDefinitionList(List<ConsentDefinition> consentDefinitionList) {
        this.consentDefinitionList = consentDefinitionList == null ? new ArrayList<ConsentDefinition>() : consentDefinitionList;
    }
}
