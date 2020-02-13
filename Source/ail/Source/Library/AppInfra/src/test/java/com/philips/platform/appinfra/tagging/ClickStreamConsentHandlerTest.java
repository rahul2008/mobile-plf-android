package com.philips.platform.appinfra.tagging;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClickStreamConsentHandlerTest {

    private static final int TEST_VERSION = 1;
    @Mock
    AppInfraInterface appInfra;
    @Mock
    SecureStorageInterface storageInterface;
    @Mock
    AppTaggingInterface appTaggingInterface;
    @Mock
    TimeInterface timeInterface;
    @Mock
    SecureStorageInterface.SecureStorageError secureStorageError;
    private TestClickStreamConsentHandler clickStreamConsentHandler;
    private String correctType = "AIL_ClickStream";
    private String wrongType = "abc";

    private long sampleTimestamp = 1531045167156l;
    private String sampleDateTime = "2018-07-08 10:19:27.156 +0000";

    @Before
    public void setUp() {
        clickStreamConsentHandler = new TestClickStreamConsentHandler(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
        when(appInfra.getTagging()).thenReturn(appTaggingInterface);
        when(storageInterface.storeValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_VERSION, String.valueOf(TEST_VERSION), secureStorageError)).thenReturn(true);
        when(storageInterface.fetchValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_VERSION, secureStorageError)).thenReturn(String.valueOf(TEST_VERSION));
        when(storageInterface.fetchValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_TIMESTAMP, secureStorageError)).thenReturn(sampleDateTime);
    }

    @Test
    public void verifyPostConsentSuccess() {
        PostConsentTypeCallback mockCallback = mock(PostConsentTypeCallback.class);

        clickStreamConsentHandler.storeConsentTypeState(correctType, true, 1, mockCallback);

        verify(mockCallback).onPostConsentSuccess();
    }

    @Test(expected = AssertionError.class)
    public void verifyPostConsentFailure() {
        PostConsentTypeCallback mockCallback = mock(PostConsentTypeCallback.class);

        clickStreamConsentHandler.storeConsentTypeState(wrongType, true, 1, mockCallback);
    }

    @Test(expected = AssertionError.class)
    public void verifyFetchConsentFailure() {

        FetchConsentTypeStateCallback mockCallback = mock(FetchConsentTypeStateCallback.class);

        clickStreamConsentHandler.fetchConsentTypeState(wrongType, mockCallback);
    }

    @Test
    public void verifyFetchRejectedConsentSuccess() {
        when(appTaggingInterface.getPrivacyConsent()).thenReturn(AppTaggingInterface.PrivacyStatus.OPTOUT);

        FetchConsentTypeStateCallback mockedCallback = mock(FetchConsentTypeStateCallback.class);
        clickStreamConsentHandler.fetchConsentTypeState(correctType, mockedCallback);

        ArgumentCaptor<ConsentStatus> captor = ArgumentCaptor.forClass(ConsentStatus.class);
        verify(mockedCallback).onGetConsentsSuccess(captor.capture());
        ConsentStatus value = captor.getValue();

        assertEquals(ConsentStates.rejected, value.getConsentState() );
        assertEquals(sampleTimestamp, value.getTimestamp().getTime());
        assertEquals(TEST_VERSION, value.getVersion());
    }

    @Test
    public void verifyFetchActiveConsentSuccess() {
        when(appTaggingInterface.getPrivacyConsent()).thenReturn(AppTaggingInterface.PrivacyStatus.OPTIN);

        FetchConsentTypeStateCallback mockedCallback = mock(FetchConsentTypeStateCallback.class);
        clickStreamConsentHandler.fetchConsentTypeState(correctType, mockedCallback);

        ArgumentCaptor<ConsentStatus> captor = ArgumentCaptor.forClass(ConsentStatus.class);
        verify(mockedCallback).onGetConsentsSuccess(captor.capture());
        ConsentStatus value = captor.getValue();

        assertEquals(ConsentStates.active, value.getConsentState());
        assertEquals(sampleTimestamp, value.getTimestamp().getTime());
        assertEquals(TEST_VERSION, value.getVersion());
    }

    @Test
    public void canReadBackwardsCompatibleConsentsWithTimeStamp() {
        when(appTaggingInterface.getPrivacyConsent()).thenReturn(AppTaggingInterface.PrivacyStatus.OPTOUT);
        when(storageInterface.fetchValueForKey(ClickStreamConsentHandler.CLICKSTREAM_CONSENT_TIMESTAMP, secureStorageError)).thenReturn(String.valueOf(sampleTimestamp));
        
        FetchConsentTypeStateCallback mockedCallback = mock(FetchConsentTypeStateCallback.class);
        clickStreamConsentHandler.fetchConsentTypeState(correctType, mockedCallback);

        ArgumentCaptor<ConsentStatus> captor = ArgumentCaptor.forClass(ConsentStatus.class);
        verify(mockedCallback).onGetConsentsSuccess(captor.capture());
        ConsentStatus value = captor.getValue();

        assertEquals(ConsentStates.rejected, value.getConsentState());
        assertEquals(sampleTimestamp, value.getTimestamp().getTime());
        assertEquals(TEST_VERSION, value.getVersion());
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

        @Override
        Date getUTCTime(){
           return new Date();
        }
    }
}
