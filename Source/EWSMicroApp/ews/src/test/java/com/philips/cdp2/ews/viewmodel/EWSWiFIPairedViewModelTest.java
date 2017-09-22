/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.ScreenFlowController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSCallbackNotifier.class})

public class EWSWiFIPairedViewModelTest {

    @Mock
    private ScreenFlowController screenFlowControllerMock;

    @Mock
    private EWSCallbackNotifier callbackNotifierMock;

    private EWSWiFIPairedViewModel viewModel;

    @Before
    public void setup() {
        initMocks(this);

        PowerMockito.mockStatic(EWSCallbackNotifier.class);
        when(EWSCallbackNotifier.getInstance()).thenReturn(callbackNotifierMock);

        viewModel = new EWSWiFIPairedViewModel(screenFlowControllerMock);
    }

    @Test
    public void shouldGiveOnSuccessCallbackWhenClickedOnStartButton() throws Exception {
        viewModel.onStartClicked();

        verify(screenFlowControllerMock).finish();
        verify(callbackNotifierMock).onSuccess();
    }

}