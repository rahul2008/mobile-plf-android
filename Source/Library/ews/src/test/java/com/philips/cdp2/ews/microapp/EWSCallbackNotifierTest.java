/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSCallbackNotifierTest {

    @Mock
    private EWSCallback ewsCallback;
    private EWSCallbackNotifier callbackNotifier;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        callbackNotifier = EWSCallbackNotifier.getInstance();
        callbackNotifier.setCallback(ewsCallback);
    }

    @Test
    public void itShouldCallOnSuccessWhenNotified() throws Exception {

        callbackNotifier.onSuccess();

        verify(ewsCallback).onSuccess();
    }

    @Test
    public void itShouldCallOnApplianceDiscoveredWhenPairingSuccessful() throws Exception {
        final String cppId = "testId";
        callbackNotifier.onApplianceDiscovered(cppId);

        verify(ewsCallback).onApplianceDiscovered(cppId);
    }

    @Test
    public void itShouldCallOnCancelWhenNotified() throws Exception {
        callbackNotifier.onCancel();

        verify(ewsCallback).onCancel();
    }

    @Test
    public void itShouldCallOnBackPressedWhenNotified() throws Exception {
        callbackNotifier.onBackPressed();

        verify(ewsCallback).onBackPressed();
    }
}