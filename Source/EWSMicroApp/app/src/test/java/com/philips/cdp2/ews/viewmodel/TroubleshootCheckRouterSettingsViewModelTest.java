/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.common.util.Tagger;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.view.EWSProductSupportFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Tagger.class)
public class TroubleshootCheckRouterSettingsViewModelTest {

    @Mock
    private ScreenFlowController sfcMock;
    private TroubleshootCheckRouterSettingsViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(Tagger.class);
        viewModel = new TroubleshootCheckRouterSettingsViewModel(sfcMock);
    }

    @Test
    public void shouldTagRouterSettingsWhenAsked() throws Exception {
        viewModel.tagWifiRouterSettings();

        PowerMockito.verifyStatic();
        Tagger.trackActionSendData(eq(Tag.KEY.TECHNICAL_ERROR), eq(Tag.VALUE.WIFI_ROUTER_ERROR));
    }

    @Test
    public void shouldShowConsumerCareScreenWhenClickedOnContactUsButton() throws Exception {
        viewModel.contactUS();

        verify(sfcMock).showFragment(isA(EWSProductSupportFragment.class));
    }
}