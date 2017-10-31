/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChooseSetupStateViewModelTest {

    @Mock
    private ScreenFlowController screenFlowControllerMock;
    private ChooseSetupStateViewModel subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject = new ChooseSetupStateViewModel(screenFlowControllerMock);
    }

    @Test
    public void shouldShowResetDeviceScreenWhenFreshSetupIsChosen() throws Exception {
        subject.onFreshSetupIsChosen();

        verify(screenFlowControllerMock).showFragment(isA(EWSResetDeviceFragment.class));
    }

    @Test
    public void shouldShowBlinkingAccessPointScreenWhenExistingSetupIsChosen() throws Exception {
        subject.onExistingSetupIsChosen();

        verify(screenFlowControllerMock).showFragment(isA(BlinkingAccessPointFragment.class));
    }
}