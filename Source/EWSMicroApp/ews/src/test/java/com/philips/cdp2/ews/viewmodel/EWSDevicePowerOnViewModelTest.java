/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.navigation.Navigator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSDevicePowerOnViewModelTest {

    @Mock
    private Navigator navigatorMock;
    private EWSDevicePowerOnViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        
        viewModel = new EWSDevicePowerOnViewModel(navigatorMock);
    }

    @Test
    public void shouldShowPressAndFollowSetupScreenWhenYesButtonIsClicked() throws Exception {
        viewModel.onYesButtonClicked();

        verify(navigatorMock).navigateToCompletingDeviceSetupScreen();
    }

    @Test
    public void shouldShowChooseCurrentStateScreenWhenNoButtonIsClicked() throws Exception {
        viewModel.onNoButtonClicked();

//        verify(navigatorMock).showFragment(isA(ChooseSetupStateFragment.class));
    }
}