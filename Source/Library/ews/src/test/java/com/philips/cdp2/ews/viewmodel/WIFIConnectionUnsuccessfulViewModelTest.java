/*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.troubleshooting.hotspotconnectionfailure.ConnectionUnsuccessfulViewModel;
import com.philips.cdp2.ews.troubleshooting.hotspotconnectionfailure.UnsuccessfulConnectionCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSTagger.class, EWSDependencyProvider.class})
public class WIFIConnectionUnsuccessfulViewModelTest {

    private ConnectionUnsuccessfulViewModel subject;

    @Mock private UnsuccessfulConnectionCallback mockCallback;
    @Mock private EWSDependencyProvider mockEWSDependencyProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSTagger.class);
        PowerMockito.mockStatic(EWSDependencyProvider.class);
        PowerMockito.when(EWSDependencyProvider.getInstance()).thenReturn(mockEWSDependencyProvider);
        PowerMockito.when(mockEWSDependencyProvider.getProductName()).thenReturn("EWS");
        subject = new ConnectionUnsuccessfulViewModel();
        subject.setCallback(mockCallback);
    }

    @Test
    public void shouldDismissDialogOnListenerOnDismissDialog() {
        subject.onNeedHelpButtonClicked();

        verify(mockCallback).hideDialogWithResult(ConnectionUnsuccessfulViewModel.HELP_NEEDED_RESULT);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTagConnectionUnsuccessfulViewWhenHelpNeededButtonClicked() throws Exception {
        ArgumentCaptor<HashMap> mapArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        subject.onNeedHelpButtonClicked();

        PowerMockito.verifyStatic();
        EWSTagger.trackAction(eq(Tag.ACTION.CONNECTION_UNSUCCESSFUL), mapArgumentCaptor.capture());
        HashMap map = mapArgumentCaptor.getValue();

        assertEquals(map.size(), 3);
        assertEquals(Tag.VALUE.CONN_ERROR_NOTIFICATION, map.get(Tag.KEY.IN_APP_NOTIFICATION));
        assertEquals(EWSDependencyProvider.getInstance().getProductName(), map.get(Tag.KEY.CONNECTED_PRODUCT_NAME));
        assertEquals(Tag.VALUE.WIFI_SINGLE_ERROR, map.get(Tag.KEY.TECHNICAL_ERROR));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTagConnectionUnsuccessfulViewWhenNoHelpNeededButtonClicked() throws Exception {
        ArgumentCaptor<HashMap> mapArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        subject.onNoHelpNeededButtonClicked();

        PowerMockito.verifyStatic();
        EWSTagger.trackAction(eq(Tag.ACTION.CONNECTION_UNSUCCESSFUL), mapArgumentCaptor.capture());
        HashMap map = mapArgumentCaptor.getValue();

        assertEquals(map.size(), 3);
        assertEquals(Tag.VALUE.CONN_ERROR_NOTIFICATION, map.get(Tag.KEY.IN_APP_NOTIFICATION));
        assertEquals(EWSDependencyProvider.getInstance().getProductName(), map.get(Tag.KEY.CONNECTED_PRODUCT_NAME));
        assertEquals(Tag.VALUE.WIFI_SINGLE_ERROR, map.get(Tag.KEY.TECHNICAL_ERROR));
    }

}