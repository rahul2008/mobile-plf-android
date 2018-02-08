package com.philips.platform.mya.catk.clickstream;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClickStreamConsentHandlerTest {

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


    private ClickStreamConsentHandler clickStreamConsentHandler;

    @Before
    public void setUp(){
        clickStreamConsentHandler = new ClickStreamConsentHandler(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
        when(appInfra.getTagging()).thenReturn(appTaggingInterface);
        when(storageInterface.storeValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_TYPE, "true", secureStorageError)).thenReturn(true);
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
    public void verifyPostConsentSuccess(){
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
    public void verifyPostConsentFailure(){
        when(definition.getTypes()).thenReturn(getWrongTypes());
        when(definition.getVersion()).thenReturn(1);
        clickStreamConsentHandler.storeConsentState(definition, true, new TestPostCallback() {
        });
    }

    private class TestPostCallback implements PostConsentCallback{

        @Override
        public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
            throw new RuntimeException("onPostConsentFailed Failed");
        }

        @Override
        public void onPostConsentSuccess(Consent consent) {
            throw new RuntimeException("onPostConsentSuccess Failed");
        }
    }


}
