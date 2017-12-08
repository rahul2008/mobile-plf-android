/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.mock;

import android.content.BroadcastReceiver;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ServiceDiscoveryInterfaceMock implements ServiceDiscoveryInterface {

    public String getServiceLocaleWithCountryPreference_return = "en_US";

    public URL url;

    @Override
    public void getHomeCountry(OnGetHomeCountryListener onGetHomeCountryListener) {

    }

    @Override
    public String getHomeCountry() {
        return null;
    }

    @Override
    public void setHomeCountry(String s) {

    }

    @Override
    public void getServiceUrlWithLanguagePreference(String s, OnGetServiceUrlListener onGetServiceUrlListener) {

    }

    @Override
    public void getServiceUrlWithLanguagePreference(String s, OnGetServiceUrlListener onGetServiceUrlListener, Map<String, String> map) {
        onGetServiceUrlListener.onSuccess(url);
    }

    @Override
    public void getServicesWithLanguagePreference(ArrayList<String> arrayList, OnGetServiceUrlMapListener onGetServiceUrlMapListener) {
        onGetServiceUrlMapListener.onSuccess(null);
    }

    @Override
    public void getServicesWithLanguagePreference(ArrayList<String> arrayList, OnGetServiceUrlMapListener onGetServiceUrlMapListener, Map<String, String> map) {

    }

    @Override
    public void getServiceUrlWithCountryPreference(String s, OnGetServiceUrlListener onGetServiceUrlListener) {

    }

    @Override
    public void getServiceUrlWithCountryPreference(String s, OnGetServiceUrlListener onGetServiceUrlListener, Map<String, String> map) {

    }

    @Override
    public void getServicesWithCountryPreference(ArrayList<String> arrayList, OnGetServiceUrlMapListener onGetServiceUrlMapListener) {

    }

    @Override
    public void getServicesWithCountryPreference(ArrayList<String> arrayList, OnGetServiceUrlMapListener onGetServiceUrlMapListener, Map<String, String> map) {

    }

    @Override
    public void getServiceLocaleWithLanguagePreference(String s, OnGetServiceLocaleListener onGetServiceLocaleListener) {

    }

    @Override
    public void getServiceLocaleWithCountryPreference(String s, OnGetServiceLocaleListener onGetServiceLocaleListener) {
    }

    @Override
    public URL applyURLParameters(URL url, Map<String, String> map) {
        return null;
    }

    @Override
    public void refresh(OnRefreshListener onRefreshListener) {

    }

    @Override
    public void refresh(OnRefreshListener onRefreshListener, boolean b) {

    }

    @Override
    public void unRegisterHomeCountrySet(BroadcastReceiver broadcastReceiver) {

    }

    @Override
    public void registerOnHomeCountrySet(BroadcastReceiver broadcastReceiver) {

    }
}
