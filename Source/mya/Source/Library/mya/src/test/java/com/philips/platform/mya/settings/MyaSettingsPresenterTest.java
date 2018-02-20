/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.mya.catk.ConsentsClient;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.CswLaunchInput;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import android.content.Context;
import android.os.Bundle;

public class MyaSettingsPresenterTest {

    private MyaSettingsContract.View view;
    private Context context;
    private MyaSettingsPresenter myaSettingsPresenter;

    @Before
    public void setup() {
        view = mock(MyaSettingsContract.View.class);
        context = mock(Context.class);
        when(view.getContext()).thenReturn(context);
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
        myaSettingsPresenter.getSettingItems(appInfraInterface, error);
        verify(view).showSettingsItems(ArgumentMatchers.<String, SettingsModel>anyMap());
        myaSettingsPresenter.getSettingItems(appInfraInterface, error, getArguments());
        verify(view).showSettingsItems(anyMap());
    }

    @Test
    public void testHandleLogOut() {
        LogoutHandler logoutHandler = myaSettingsPresenter.getLogoutHandler();
        Bundle bundle = new Bundle();
        UserDataModelProvider userDataModelProvider = new UserDataModelProvider(context);
        bundle.putSerializable(USER_PLUGIN, userDataModelProvider);
        myaSettingsPresenter.logOut(bundle);
        logoutHandler.onLogoutSuccess();
        verify(view).onLogOutSuccess();
    }

    @Test
    public void testHandleOnClickSettingsItem() {
        MyaDependencies mockDependencies = mock(MyaDependencies.class);
        AppInfraInterface mockAppInfra = mock(AppInfraInterface.class);
        when(mockDependencies.getAppInfra()).thenReturn(mockAppInfra);
        RestInterface mockRestClient = mock(RestInterface.class);
        when(mockRestClient.isInternetReachable()).thenReturn(true);
        when(mockAppInfra.getRestClient()).thenReturn(mockRestClient);
        MyaInterface.get().init(mockDependencies, new UappSettings(view.getContext()));
        final CswInterface cswInterface = mock(CswInterface.class);
        final ConsentsClient consentsClient = mock(ConsentsClient.class);
        final CswLaunchInput cswLaunchInput = mock(CswLaunchInput.class);
        final FragmentLauncher fragmentLauncher = mock(FragmentLauncher.class);
        myaSettingsPresenter = new MyaSettingsPresenter(view) {
            @Override
            CswInterface getCswInterface() {
                return cswInterface;
            }

            @Override
            CswLaunchInput buildLaunchInput(boolean addToBackStack, Context context) {
                return cswLaunchInput;
            }

            @Override
            ConsentsClient getConsentsClient() {
                return consentsClient;
            }
        };
        String key = "Mya_Privacy_Settings";
        assertTrue(myaSettingsPresenter.handleOnClickSettingsItem(key, fragmentLauncher));
        verify(cswInterface).launch(fragmentLauncher, cswLaunchInput);
        assertFalse(myaSettingsPresenter.handleOnClickSettingsItem("some_key", fragmentLauncher));
    }

    @Test
    public void testHandleOnClickSettingsWhenDeviceIsOffline() {
        MyaDependencies mockDependencies = mock(MyaDependencies.class);
        AppInfraInterface mockAppInfra = mock(AppInfraInterface.class);
        when(mockDependencies.getAppInfra()).thenReturn(mockAppInfra);
        RestInterface mockRestClient = mock(RestInterface.class);
        when(mockRestClient.isInternetReachable()).thenReturn(false);
        when(mockAppInfra.getRestClient()).thenReturn(mockRestClient);
        MyaInterface.get().init(mockDependencies, new UappSettings(view.getContext()));
        String testTitle = "Test title";
        when(context.getString(R.string.MYA_Offline_title)).thenReturn(testTitle);
        String testMessage = "Test message";
        when(context.getString(R.string.MYA_Offline_message)).thenReturn(testMessage);
        final FragmentLauncher fragmentLauncher = mock(FragmentLauncher.class);
        myaSettingsPresenter = new MyaSettingsPresenter(view);
        String key = "Mya_Privacy_Settings";
        assertFalse(myaSettingsPresenter.handleOnClickSettingsItem(key, fragmentLauncher));
        verify(view).showOfflineDialog(testTitle, testMessage);
    }

    @Test
    public void shouldNotReturnNullWhenInvoked() {
        MyaHelper.getInstance().setConfigurations(new ArrayList<ConsentConfiguration>());
        assertNotNull(myaSettingsPresenter.buildLaunchInput(false, view.getContext()));
        assertNotNull(myaSettingsPresenter.getCswInterface());
        assertNotNull(myaSettingsPresenter.getConsentsClient());
    }

    private Bundle getArguments() {
        Bundle arguments = new Bundle();
        MyaLaunchInput value = new MyaLaunchInput(context, null);
        String[] profileItems = {"profile1","profile2"};
        value.setProfileConfigurableItems(Arrays.asList(profileItems));
        return arguments;
    }
}