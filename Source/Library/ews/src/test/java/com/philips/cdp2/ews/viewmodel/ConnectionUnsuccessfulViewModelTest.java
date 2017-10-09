/*
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.v4.app.DialogFragment;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.troubleshooting.connectionfailure.ConnectionUnsuccessfulViewModel;

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
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSTagger.class, EWSDependencyProvider.class})
public class ConnectionUnsuccessfulViewModelTest {

    @Mock
    private DialogFragment dialogFragmentMock;
    @Mock
    private EWSDependencyProvider ewsDependencyProviderMock;

    private ConnectionUnsuccessfulViewModel subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(EWSTagger.class);
        PowerMockito.mockStatic(EWSDependencyProvider.class);

        when(EWSDependencyProvider.getInstance()).thenReturn(ewsDependencyProviderMock);
        when(ewsDependencyProviderMock.getProductName()).thenReturn("product leet");

        subject = new ConnectionUnsuccessfulViewModel();
        subject.setDialogDismissListener(dialogFragmentMock);
    }

    @Test
    public void shouldDismissDialogOnListenerOnDismissDialog() {
        subject.onNeedHelpButtonClicked();

        verify(dialogFragmentMock).dismissAllowingStateLoss();
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionOnListenerNotAttached() {
        subject = new ConnectionUnsuccessfulViewModel();

        subject.onNeedHelpButtonClicked();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTagConnectionUnsuccessfulViewWhenDismissed() throws Exception {
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

    @Test
    public void ShouldEnsureDialogDismissListenerIsNullOnceRemoved() throws Exception {
        subject.removeDialogDismissListener();

        assertNull(subject.getDialogDismissListener());
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionOnListenerRemovedAndDialogDismissed() throws Exception {
        subject.removeDialogDismissListener();
        subject.onNeedHelpButtonClicked();
    }

}