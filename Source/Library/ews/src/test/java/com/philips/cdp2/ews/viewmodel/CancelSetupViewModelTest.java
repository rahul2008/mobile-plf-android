/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.ScreenFlowController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSCallbackNotifier.class})
public class CancelSetupViewModelTest {

    @Mock
    private ScreenFlowController screenFlowControllerMock;

    @Mock
    private DialogFragment dialogFragmentMock;

    @Mock
    private EWSCallbackNotifier callbackNotifierMock;

    private CancelSetupViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(EWSCallbackNotifier.class);
        when(EWSCallbackNotifier.getInstance()).thenReturn(callbackNotifierMock);

        viewModel = new CancelSetupViewModel(screenFlowControllerMock);
        viewModel.setDialogDismissListener(dialogFragmentMock);
    }

    @Test
    public void shouldCancelEWSSetupWhenClickedOnYesButtonInCancelSetupDialog() throws Exception {
        viewModel.cancelSetup();

        InOrder inOrder = inOrder(callbackNotifierMock, screenFlowControllerMock);

        inOrder.verify(callbackNotifierMock).onCancel();
        inOrder.verify(screenFlowControllerMock).finish();
    }

    @Test
    public void shouldDismissCancelSetupDialogBoxWhenClickedOnNOButton() throws Exception {
        viewModel.dismissDialog();

        verify(dialogFragmentMock).dismissAllowingStateLoss();
    }

    @Test
    public void shouldRemoveDialogDismissListenerWhenAsked() throws Exception {
        viewModel.removeDialogDismissListener();

        verifyZeroInteractions(dialogFragmentMock);
    }
}