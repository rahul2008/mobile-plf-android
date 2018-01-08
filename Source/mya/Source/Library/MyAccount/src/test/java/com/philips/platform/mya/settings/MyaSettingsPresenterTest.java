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
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.CatkInputs;
import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.consenthandlerinterface.ConsentHandlerMapping;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.MyaHelper;
import com.philips.platform.mya.R;
import com.philips.platform.myaplugin.user.UserDataModelProvider;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.philips.platform.mya.launcher.MyaInterface.USER_PLUGIN;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyMap;
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
        verify(view).showSettingsItems(anyMap());
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

    @Test
    public void testHandleLogOut() {
        LogoutHandler logoutHandler = myaSettingsPresenter.getLogoutHandler();
        Bundle bundle = new Bundle();
        UserDataModelProvider userDataModelProvider = new UserDataModelProvider(context);
        bundle.putSerializable(USER_PLUGIN, userDataModelProvider);
        myaSettingsPresenter.logOut(bundle);
        logoutHandler.onLogoutSuccess();
        verify(view).handleLogOut();
    }

    @Test
    public void testHandleOnClickSettingsItem() {
        final CswInterface cswInterface = mock(CswInterface.class);
        final ConsentAccessToolKit consentAccessToolKit = mock(ConsentAccessToolKit.class);
        final CswLaunchInput cswLaunchInput = mock(CswLaunchInput.class);
        final CatkInputs catkInputs = mock(CatkInputs.class);
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
            ConsentAccessToolKit getConsentAccessInstance() {
                return consentAccessToolKit;
            }
        };
        String key = "Mya_Privacy_Settings";
        assertTrue(myaSettingsPresenter.handleOnClickSettingsItem(key, fragmentLauncher));
        verify(cswInterface).launch(fragmentLauncher, cswLaunchInput);
        assertFalse(myaSettingsPresenter.handleOnClickSettingsItem("some_key", fragmentLauncher));
        myaSettingsPresenter.handleOnClickSettingsItem("Mya_Privacy_Settings", null);
        verify(view).exitMyAccounts();
    }

    @Test
    public void shouldNotReturnNullWhenInvoked() {
        MyaHelper.getInstance().setConfigurations(new ArrayList<ConsentHandlerMapping>());
        assertNotNull(myaSettingsPresenter.buildLaunchInput(false, view.getContext()));
        assertNotNull(myaSettingsPresenter.getCswInterface());
        assertNotNull(myaSettingsPresenter.getConsentAccessInstance());
    }

}