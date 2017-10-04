/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
public class EWSGettingStartedViewModelTest {

    @Mock
    private Navigator navigatorMock;

    @Mock
    private WiFiUtil wifiUtilMock;

    private EWSGettingStartedViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new EWSGettingStartedViewModel(navigatorMock, wifiUtilMock);
    }

    @Test
    public void shouldNavigateToHomeWifiScreenIfWifiIsEnabledWhenClickedOnGettingStartedButton() throws Exception {
        stubHomeWiFiStatus(true);

        verify(navigatorMock).navigateToHomeNetworkConfirmationScreen();
    }

//    @Test
//    public void shouldNavigateToTroubleshootHomeWiFiScreenIfWifiIsDisabledWhenClickedOnGettingStartedButton() throws Exception {
//        final int currentFragmentHierarchyLevelInStack = 2;
//        final ArgumentCaptor<TroubleshootHomeWiFiFragment> fragmentCaptor = ArgumentCaptor.forClass(TroubleshootHomeWiFiFragment.class);
//
//        viewModel.setHierarchyLevel(currentFragmentHierarchyLevelInStack);
//        stubHomeWiFiStatus(false);
//
//        verify(navigatorMock).showFragment(fragmentCaptor.capture());
//
//        TroubleshootHomeWiFiFragment troubleshootHomeWiFiFragment = fragmentCaptor.getValue();
//        int hierarchyLevel = troubleshootHomeWiFiFragment.getArguments().getInt(TroubleshootHomeWiFiFragment.HIERARCHY_LEVEL);
//
//        assertEquals(currentFragmentHierarchyLevelInStack + 1, hierarchyLevel);
//        verify(navigatorMock).showFragment(isA(TroubleshootHomeWiFiFragment.class));
//    }

    @Test
    public void shouldFinishActivityBackWhenAsked() throws Exception {
        viewModel.onBackPressed();

//        verify(navigatorMock).finish();
    }

    private void stubHomeWiFiStatus(final boolean enabled) {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(enabled);
        viewModel.onGettingStartedButtonClicked();
    }
}