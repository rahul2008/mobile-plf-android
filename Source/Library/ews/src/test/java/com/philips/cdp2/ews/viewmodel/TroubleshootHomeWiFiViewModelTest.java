/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.common.callbacks.DialogCallback;
import com.philips.cdp2.ews.troubleshooting.homewifi.TroubleshootHomeWiFiViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TroubleshootHomeWiFiViewModelTest {

    @InjectMocks private TroubleshootHomeWiFiViewModel subject;

    @Mock private DialogCallback mockDialogCallback;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldHideDialogWhenOnCloseClicked() throws Exception {
        subject.setDialogCallback(mockDialogCallback);

        subject.onCloseButtonClicked();

        verify(mockDialogCallback).hideDialog();
    }

    @Test
    public void itShouldNotHideDialogWhenOnCloseClickedWhenCallbackIsNull() throws Exception {
        subject.setDialogCallback(null);

        subject.onCloseButtonClicked();

        verify(mockDialogCallback, never()).hideDialog();
    }
}