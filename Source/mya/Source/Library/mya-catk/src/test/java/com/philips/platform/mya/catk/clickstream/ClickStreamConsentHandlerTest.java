package com.philips.platform.mya.catk.clickstream;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClickStreamConsentHandlerTest {

    private static final String TEST_VERSION = "1";
    @Mock
    AppInfra appInfra;
    @Mock
    ConsentDefinition definition;
    @Mock
    SecureStorageInterface storageInterface;
    @Mock
    AppTaggingInterface appTaggingInterface;
    @Mock
    SecureStorageInterface.SecureStorageError secureStorageError;
    private TestClickStreamConsentHandler clickStreamConsentHandler;

    @Before
    public void setUp() {
        clickStreamConsentHandler = new TestClickStreamConsentHandler(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
        when(appInfra.getTagging()).thenReturn(appTaggingInterface);
        when(storageInterface.storeValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_VERSION, "1", secureStorageError)).thenReturn(true);
        when(storageInterface.fetchValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_VERSION, secureStorageError)).thenReturn(TEST_VERSION);
        when(definition.getLocale()).thenReturn("en-US");
    }

    private List<String> getTypes() {
        List<String> types = new ArrayList<>();
        types.add(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_TYPE);
        return types;
    }

    private List<String> getWrongTypes() {
        List<String> types = new ArrayList<>();
        types.add("abc");
        return types;
    }

    @Test
    public void verifyPostConsentSuccess() {
        when(definition.getTypes()).thenReturn(getTypes());
        when(definition.getVersion()).thenReturn(1);
        clickStreamConsentHandler.storeConsentState(definition, true, new TestPostCallback() {
            @Override
            public void onPostConsentSuccess(Consent consent) {
                assertEquals(ConsentStatus.active, consent.getStatus());
            }
        });
    }

    @Test(expected = AssertionError.class)
    public void verifyPostConsentFailure() {
        when(definition.getTypes()).thenReturn(getWrongTypes());
        clickStreamConsentHandler.storeConsentState(definition, true, new TestPostCallback() {
        });
    }

    @Test(expected = AssertionError.class)
    public void verifyFetchConsentFailure() {
        when(definition.getTypes()).thenReturn(getWrongTypes());
        clickStreamConsentHandler.fetchConsentState(definition, new TestFetchCallback() {
        });
    }

    @Test
    public void verifyFetchConsentSuccess() {
        when(definition.getTypes()).thenReturn(getTypes());
        when(definition.getVersion()).thenReturn(1);
        when(appTaggingInterface.getPrivacyConsent()).thenReturn(AppTaggingInterface.PrivacyStatus.OPTOUT);
        clickStreamConsentHandler.fetchConsentStates(Collections.singletonList(definition), new TestFetchCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                assertEquals(false, consents.get(0).isAccepted());
            }
        });
    }

    @Test
    public void verifyFetchConsentVersionMismatch() {
        when(definition.getTypes()).thenReturn(getTypes());
        when(definition.getVersion()).thenReturn(2);
        clickStreamConsentHandler.fetchConsentState(definition, new TestFetchCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                assertEquals(false, consents.get(0).isAccepted());
            }
        });
    }

    private class TestPostCallback implements PostConsentCallback {

        @Override
        public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
            throw new RuntimeException("onPostConsentFailed Failed");
        }

        @Override
        public void onPostConsentSuccess(Consent consent) {
            throw new RuntimeException("onPostConsentSuccess Failed");
        }
    }

    private class TestFetchCallback implements CheckConsentsCallback {

        @Override
        public void onGetConsentsSuccess(List<Consent> consents) {
            throw new RuntimeException("onGetConsentsSuccess Failed");
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            throw new RuntimeException("onGetConsentsFailed Failed");
        }
    }

    private class TestClickStreamConsentHandler extends ClickStreamConsentHandler {

        TestClickStreamConsentHandler(AppInfra appInfra) {
            super(appInfra);
        }

        @NonNull
        @Override
        SecureStorageInterface.SecureStorageError getSecureStorageError() {
            return secureStorageError;
        }

    }
}
