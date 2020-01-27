/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.settings;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyaSettingsPresenterTest {

    private MyaSettingsContract.View view;
    private Context context;
    private MyaSettingsPresenter myaSettingsPresenter;

    @Before
    public void setup() {
        view = mock(MyaSettingsContract.View.class);
        context = mock(Context.class);
        FragmentActivity fragmentActivityMock = mock(FragmentActivity.class);
        when(view.getFragmentActivity()).thenReturn(fragmentActivityMock);
        myaSettingsPresenter = new MyaSettingsPresenter(view);
        MyaSettingsFragment myaSettingsFragment = new MyaSettingsFragment();
        myaSettingsPresenter.onViewActive(myaSettingsFragment);
        myaSettingsPresenter.onViewInactive();
        assertNull(myaSettingsPresenter.getView());
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
        MyaHelper.getInstance().setMyaLaunchInput(new MyaLaunchInput(context));
        myaSettingsPresenter.getSettingItems(appInfraInterface, error);
        verify(view).showSettingsItems(ArgumentMatchers.<String, SettingsModel>anyMap());
    }

    @Test
    public void testHandleOnClickSettingsItem() {
        MyaDependencies mockDependencies = mock(MyaDependencies.class);
        AppInfraInterface mockAppInfra = mock(AppInfraInterface.class);
        when(mockDependencies.getAppInfra()).thenReturn(mockAppInfra);
        RestInterface mockRestClient = mock(RestInterface.class);
        when(mockRestClient.isInternetReachable()).thenReturn(true);
        when(mockAppInfra.getRestClient()).thenReturn(mockRestClient);
        LoggingInterface mockLoggingInterface = mock(LoggingInterface.class);
        when(mockAppInfra.getLogging()).thenReturn(mockLoggingInterface);
        AppTaggingInterface appTaggingInterfaceMock = mock(AppTaggingInterface.class);
        when(mockAppInfra.getTagging()).thenReturn(appTaggingInterfaceMock);
        final FragmentLauncher fragmentLauncher = mock(FragmentLauncher.class);
        myaSettingsPresenter = new MyaSettingsPresenter(view);
        String key = "Mya_Privacy_Settings";
        assertFalse(myaSettingsPresenter.handleOnClickSettingsItem(key, fragmentLauncher));
        key = "MYA_My_details";
        assertFalse(myaSettingsPresenter.handleOnClickSettingsItem(key, fragmentLauncher));
    }
}