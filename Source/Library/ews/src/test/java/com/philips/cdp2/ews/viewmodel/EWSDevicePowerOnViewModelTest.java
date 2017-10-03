/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.ChooseSetupStateFragment;
import com.philips.cdp2.ews.view.EWSPressPlayAndFollowSetupFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSDevicePowerOnViewModelTest {

    @Mock
    private ScreenFlowController screenFlowControllerMock;
    private EWSDevicePowerOnViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        
        viewModel = new EWSDevicePowerOnViewModel(screenFlowControllerMock);
    }

    @Test
    public void shouldShowPressAndFollowSetupScreenWhenYesButtonIsClicked() throws Exception {
        viewModel.onYesButtonClicked();

        verify(screenFlowControllerMock).showFragment(isA(EWSPressPlayAndFollowSetupFragment.class));
    }

    @Test
    public void shouldShowChooseCurrentStateScreenWhenNoButtonIsClicked() throws Exception {
        viewModel.onNoButtonClicked();

        verify(screenFlowControllerMock).showFragment(isA(ChooseSetupStateFragment.class));
    }
}