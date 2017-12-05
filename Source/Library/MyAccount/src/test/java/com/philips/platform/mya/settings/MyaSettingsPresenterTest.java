/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.settings;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.mya.R;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyaSettingsPresenterTest {

    MyaSettingsContract.View view;
    Context context;
    private MyaSettingsPresenter myaSettingsPresenter;

    @Before
    public void setup() {
        view = mock(MyaSettingsContract.View.class);
        context = mock(Context.class);
        when(view.getContext()).thenReturn(context);
        myaSettingsPresenter = new MyaSettingsPresenter(view);
    }

    @Test
    public void testGettingSettingItems() {
        AppInfraInterface appInfraInterface = mock(AppInfraInterface.class);
        when(context.getString(R.string.MYA_Country)).thenReturn("some country");
        when(context.getString(R.string.MYA_change_country_message)).thenReturn("some message");
        ArrayList arrayList = new ArrayList();
        AppConfigurationInterface.AppConfigurationError error = new AppConfigurationInterface.AppConfigurationError();
        AppConfigurationInterface appConfigurationInterface = mock(AppConfigurationInterface.class);
        ServiceDiscoveryInterface serviceDiscoveryInterface = mock(ServiceDiscoveryInterface.class);
        when(serviceDiscoveryInterface.getHomeCountry()).thenReturn("some country");
        when(appConfigurationInterface.getPropertyForKey("settings.menuItems", "mya", error)).thenReturn(arrayList);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryInterface);
        myaSettingsPresenter.getSettingItems(appInfraInterface, error);
        Map dataModelLinkedHashMap = anyMap();
        verify(view).showSettingsItems(dataModelLinkedHashMap);

    }

    @Test
    public void testOnClickRecyclerItem() {
        String key = "MYA_Country";
        when(context.getString(R.string.MYA_change_country)).thenReturn("some title");
        when(context.getString(R.string.MYA_change_country_message)).thenReturn("some message");
        SettingsModel settingsModel = new SettingsModel();
        myaSettingsPresenter.onClickRecyclerItem(key, settingsModel);
        verify(view).showDialog("some title", "some message");
    }


}