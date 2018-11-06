/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfig;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSMicroAppInterfaceImplTest {

    THSMicroAppInterfaceImpl mThsMicroAppInterface;

    @Mock
    UappDependencies uappDependenciesMock;

    @Mock
    UappSettings uappSettingsMock;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    Context contextMock;

    @Mock
    FragmentLauncher uiFragmentLauncherMock;

    @Mock
    THSMicroAppLaunchInput uiHSMicroAppLaunchInputMock;

    @Mock
    UappLaunchInput uappLaunchInputMock;

    @Mock
    AppTaggingInterface appTaggingInterfaceMock;

    @Mock
    LoggingInterface loggingInterfaceMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    FragmentActivity fragmentActivityMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    FragmentTransaction fragmentTransactionMock;

    @Mock
    ActivityLauncher uiActivityLauncherMock;

    @Mock
    ThemeConfiguration themeConfigurationMock;

    @Mock
    ColorRange themeConfigColorRangeMock;

    @Mock
    ContentColor themeConfigContentColorMock;


    @Mock
    NavigationColor themeConfigNavigationColorMock;

    @Mock
    AccentRange themeConfigAssentRangeMock;

    @Mock
    ConsentManagerInterface consentManagerInterfaceMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterfaceMock);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterfaceMock);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterface);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterfaceMock);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterfaceMock);
        when(appInfraInterfaceMock.getConsentManager()).thenReturn(consentManagerInterfaceMock);

        when(uiFragmentLauncherMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(fragmentActivityMock.getSupportFragmentManager()).thenReturn(fragmentManagerMock);
        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);
        when(fragmentTransactionMock.replace(anyInt(),any(THSBaseFragment.class),anyString())).thenReturn(fragmentTransactionMock);
        when(fragmentTransactionMock.addToBackStack(anyString())).thenReturn(fragmentTransactionMock);

        when(uappDependenciesMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(uappSettingsMock.getContext()).thenReturn(contextMock);

        mThsMicroAppInterface = new THSMicroAppInterfaceImpl();
        mThsMicroAppInterface.appInfra = appInfraInterfaceMock;
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void init() throws Exception {

        mThsMicroAppInterface.init(uappDependenciesMock,uappSettingsMock);

        assertNotNull(mThsMicroAppInterface.context);
        assertNotNull(mThsMicroAppInterface.appInfra);
    }

    @Test
    public void launch() throws Exception {
        mThsMicroAppInterface.launch(uiFragmentLauncherMock,uappLaunchInputMock);

    }

    @Test
    public void launchMicroAppWithCallBack(){
        mThsMicroAppInterface.launch(uiFragmentLauncherMock,uiHSMicroAppLaunchInputMock);
    }

    @Test
    public void launchActivity() throws Exception {
        mThsMicroAppInterface.context = contextMock;
        mThsMicroAppInterface.launch(uiActivityLauncherMock,uappLaunchInputMock);
        verify(contextMock).startActivity(any(Intent.class));
    }

    @Test
    public void launchActivityThemeConfiguration() throws Exception {
        mThsMicroAppInterface.context = contextMock;
        when(uiActivityLauncherMock.getDlsThemeConfiguration()).thenReturn(themeConfigurationMock);
        List list = new ArrayList();
        list.add(themeConfigAssentRangeMock);
        list.add(themeConfigColorRangeMock);
        list.add(themeConfigContentColorMock);
        list.add(themeConfigNavigationColorMock);

        when(themeConfigurationMock.getConfigurations()).thenReturn(list);

        mThsMicroAppInterface.launch(uiActivityLauncherMock,uappLaunchInputMock);
        verify(contextMock).startActivity(any(Intent.class));
    }
}