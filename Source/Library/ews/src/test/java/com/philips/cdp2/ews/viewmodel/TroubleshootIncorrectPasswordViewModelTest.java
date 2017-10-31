/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSTagger.class, EWSDependencyProvider.class})
public class TroubleshootIncorrectPasswordViewModelTest {

    private TroubleshootIncorrectPasswordViewModel viewModel;

    @Mock
    private ScreenFlowController screenFlowControllerMock;

    @Mock
    private EWSDependencyProvider ewsDependencyProviderMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(EWSTagger.class);
        PowerMockito.mockStatic(EWSDependencyProvider.class);

        when(EWSDependencyProvider.getInstance()).thenReturn(ewsDependencyProviderMock);
        when(ewsDependencyProviderMock.getProductName()).thenReturn("product leet");

        viewModel = new TroubleshootIncorrectPasswordViewModel(screenFlowControllerMock);
    }

    @Test
    public void shouldShowWifiDisplayScreenWhenClickedOnConnectAgainButton() throws Exception {
        viewModel.connectAgain();

        verify(screenFlowControllerMock).showFragment(isA(EWSHomeWifiDisplayFragment.class));
    }

    @Test
    public void shouldShowCheckRouterSettingsScreenWhenClickedOnRouterSettingsButton() throws Exception {
        viewModel.checkRouterSettings();

        verify(screenFlowControllerMock).showFragment(isA(TroubleshootCheckRouterSettingsFragment.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTagIncorrectPasswordWhenAsked() throws Exception {
        ArgumentCaptor<HashMap> mapArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        viewModel.tagIncorrectPassword();

        PowerMockito.verifyStatic();
        EWSTagger.trackAction(eq(Tag.ACTION.CONNECTION_UNSUCCESSFUL), mapArgumentCaptor.capture());
        HashMap map = mapArgumentCaptor.getValue();

        assertEquals(map.size(), 3);
        assertEquals(Tag.VALUE.WRONG_PASSWORD_NOTIFICATION, map.get(Tag.KEY.IN_APP_NOTIFICATION));
        assertEquals(EWSDependencyProvider.getInstance().getProductName(), map.get(Tag.KEY.CONNECTED_PRODUCT_NAME));
        assertEquals(Tag.VALUE.WRONG_PASSWORD_ERROR, map.get(Tag.KEY.USER_ERROR));
    }
}