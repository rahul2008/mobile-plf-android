/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSResetDeviceViewModelTest {

    @Mock
    private ScreenFlowController screenFlowControllerMock;
    private EWSResetDeviceViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new EWSResetDeviceViewModel(screenFlowControllerMock);
    }

    @Test
    public void shouldShowEwsPowerOnDeviceScreenWhenContinueButtonIsClicked() throws Exception {
        viewModel.onContinueButtonClicked();

        verify(screenFlowControllerMock).showFragment(isA(EWSDevicePowerOnFragment.class));
    }
}