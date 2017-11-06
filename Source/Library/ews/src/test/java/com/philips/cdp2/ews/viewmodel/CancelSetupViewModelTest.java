/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.common.callbacks.FragmentCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSCallbackNotifier.class})
public class CancelSetupViewModelTest {

    @InjectMocks private CancelSetupViewModel subject;

    @Mock private FragmentCallback mockFragmentCallback;
    @Mock private DialogFragment dialogFragmentMock;
    @Mock private EWSCallbackNotifier callbackNotifierMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(EWSCallbackNotifier.class);
        when(EWSCallbackNotifier.getInstance()).thenReturn(callbackNotifierMock);

        subject.setDialogDismissListener(dialogFragmentMock);
        subject.setFragmentCallback(mockFragmentCallback);
    }

    @Test
    public void itShouldCancelEWSSetupWhenClickedOnYesButtonInCancelSetupDialog() throws Exception {
        subject.cancelSetup();

        verify(callbackNotifierMock).onCancel();
    }

    @Test
    public void itShouldFinishMicroAppOnYesButtonInCancelSetupDialog() throws Exception {
        subject.cancelSetup();

        verify(mockFragmentCallback).finishMicroApp();
    }

    @Test
    public void itShouldNotFinishMicroAppOnYesButtonInCancelSetupDialogWhenCallbackIsNull() throws Exception {
        subject.setFragmentCallback(null);

        subject.cancelSetup();

        verify(mockFragmentCallback, never()).finishMicroApp();
    }

    @Test
    public void itShouldDismissCancelSetupDialogBoxWhenClickedOnNOButton() throws Exception {
        subject.dismissDialog();

        verify(dialogFragmentMock).dismissAllowingStateLoss();
    }

    @Test
    public void itShouldNotDismissCancelSetupDialogBoxWhenClickedOnNOButtonWhenDialogIsNull() throws Exception {
        subject.setDialogDismissListener(null);

        subject.dismissDialog();

        verify(dialogFragmentMock, never()).dismissAllowingStateLoss();
    }

    @Test
    public void itShouldRemoveDialogDismissListenerWhenAsked() throws Exception {
        subject.removeDialogDismissListener();

        verifyZeroInteractions(dialogFragmentMock);
    }
}