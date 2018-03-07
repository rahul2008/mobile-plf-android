package com.philips.platform.appinfra.tagging;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClickStreamConsentHandlerTest {

    private static final String TEST_VERSION = "1";
    @Mock
    AppInfraInterface appInfra;
    @Mock
    ConsentDefinition definition;
    @Mock
    SecureStorageInterface storageInterface;
    @Mock
    AppTaggingInterface appTaggingInterface;
    @Mock
    SecureStorageInterface.SecureStorageError secureStorageError;
    @Mock
    private InternationalizationInterface internationalizationInterface;
    List<String> types;
    private TestClickStreamConsentHandler clickStreamConsentHandler;

    @Before
    public void setUp() {
        clickStreamConsentHandler = new TestClickStreamConsentHandler(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
        when(appInfra.getTagging()).thenReturn(appTaggingInterface);
        when(storageInterface.storeValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_VERSION, "1", secureStorageError)).thenReturn(true);
        when(storageInterface.fetchValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_VERSION, secureStorageError)).thenReturn(TEST_VERSION);
        when(appInfra.getInternationalization()).thenReturn(internationalizationInterface);
        types = new ArrayList<>();
        types.add("AIL_ClickStream");
    }

    private List<String> getWrongTypes() {
        List<String> types = new ArrayList<>();
        types.add("abc");
        return types;
    }

    @Test
    public void verifyPostConsentSuccess() {
        when(definition.getTypes()).thenReturn(types);
        when(definition.getVersion()).thenReturn(1);
        PostConsentCallback mockCallback = mock(PostConsentCallback.class);

        clickStreamConsentHandler.storeConsentState(definition, true, mockCallback);


        ArgumentCaptor<Consent> callbackss = ArgumentCaptor.forClass(Consent.class);
        verify(mockCallback).onPostConsentSuccess(callbackss.capture());
        Consent consent = callbackss.getValue();

        assertEquals(consent.getStatus(), ConsentStatus.active);
    }

    @Test(expected = AssertionError.class)
    public void verifyPostConsentFailure() {
        when(definition.getTypes()).thenReturn(getWrongTypes());

        PostConsentCallback mockCallback = mock(PostConsentCallback.class);

        clickStreamConsentHandler.storeConsentState(definition, true, mockCallback);


    }

    @Test(expected = AssertionError.class)
    public void verifyFetchConsentFailure() {
        when(definition.getTypes()).thenReturn(getWrongTypes());

        CheckConsentsCallback mockCallbackcs = mock(CheckConsentsCallback.class);

        clickStreamConsentHandler.fetchConsentState(definition, mockCallbackcs);
    }

    @Test
    public void verifyFetchConsentSuccess() {
        when(definition.getTypes()).thenReturn(types);
        when(definition.getVersion()).thenReturn(1);
        when(appTaggingInterface.getPrivacyConsent()).thenReturn(AppTaggingInterface.PrivacyStatus.OPTOUT);

        CheckConsentsCallback mockedCallback = mock(CheckConsentsCallback.class);
        clickStreamConsentHandler.fetchConsentStates(Collections.singletonList(definition), mockedCallback);

        ArgumentCaptor<List<Consent>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockedCallback).onGetConsentsSuccess(captor.capture());
        List<Consent> value = captor.getValue();

        assertFalse(value.get(0).isAccepted());

    }

    @Test
    public void verifyFetchConsentVersionMismatch() {
        when(definition.getTypes()).thenReturn(types);
        when(definition.getVersion()).thenReturn(2);

        CheckConsentsCallback fetcherMock = mock(CheckConsentsCallback.class);
        clickStreamConsentHandler.fetchConsentState(definition, fetcherMock);

        ArgumentCaptor<List<Consent>> captor = ArgumentCaptor.forClass(List.class);
        verify(fetcherMock).onGetConsentsSuccess(captor.capture());
        List<Consent> value = captor.getValue();

        assertFalse(value.get(0).isAccepted());

    }


    private class TestClickStreamConsentHandler extends ClickStreamConsentHandler {

        TestClickStreamConsentHandler(AppInfraInterface appInfra) {
            super(appInfra);
        }

        @NonNull
        @Override
        SecureStorageInterface.SecureStorageError getSecureStorageError() {
            return secureStorageError;
        }

    }
}
