package com.philips.platform.mya.catk.device;


import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeviceStoredConsentHandlerTest {

    private DeviceStoredConsentHandler handler;
    private String storedValueHighVersion = "true" + DeviceStoredConsentHandler.DEVICESTORE_VALUE_DELIMITER + "2" + DeviceStoredConsentHandler.DEVICESTORE_VALUE_DELIMITER + "en-US" + DeviceStoredConsentHandler.DEVICESTORE_VALUE_DELIMITER + "type3";
    private String storedValueFalseVersion = "false" + DeviceStoredConsentHandler.DEVICESTORE_VALUE_DELIMITER + "2" + DeviceStoredConsentHandler.DEVICESTORE_VALUE_DELIMITER + "en-US" + DeviceStoredConsentHandler.DEVICESTORE_VALUE_DELIMITER + "type3";

    @Mock
    AppInfra appInfra;

    @Mock
    SecureStorageInterface storageInterface;

    @Mock
    SecureStorageInterface.SecureStorageError secureStorageError;


    @Mock
    ConsentDefinition consentDefinition;

    @Mock
    LoggingInterface loggingInterface;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(consentDefinition.getTypes()).thenReturn(getConsentTypes());
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
        when(consentDefinition.getLocale()).thenReturn("en-US");
        when(appInfra.getLogging()).thenReturn(loggingInterface);
        handler = new TestDeviceStoredConsentHandler(appInfra);
    }

    @Test
    public void verifyInactiveStatusForFirstFetch() {
        when(secureStorageError.getErrorCode()).thenReturn(SecureStorageInterface.SecureStorageError.secureStorageError.NoDataFoundForKey);
        handler.fetchConsentStates(getTestConfigurationList(), new TestCheckConsentCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                assertTrue(consents.get(0).getStatus() == ConsentStatus.inactive);
            }
        });
    }

    @Test
    public void verifyErrorForKeyNotFound() {
        handler.fetchConsentStates(getTestConfigurationList(), new TestCheckConsentCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                assertEquals(ConsentStatus.inactive, consents.get(0).getStatus());
            }
        });
    }

    @Test
    public void verifyInactiveStatusForVersionMismatch() {
        when(storageInterface.fetchValueForKey(anyString(), eq(secureStorageError))).thenReturn(storedValueHighVersion);
        when(consentDefinition.getVersion()).thenReturn(3);
        handler.fetchConsentStates(getTestConfigurationList(), new TestCheckConsentCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                assertEquals(ConsentStatus.inactive, consents.get(0).getStatus());
            }
        });
    }

    @Test
    public void verifyErrorForMissingType() {
        when(storageInterface.fetchValueForKey(anyString(), eq(secureStorageError))).thenReturn(storedValueFalseVersion).thenReturn(null);
        handler.fetchConsentStates(getTestConfigurationList(), new TestCheckConsentCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                assertEquals(ConsentStatus.inactive, consents.get(0).getStatus());
            }
        });
    }

    @Test
    public void verifyActiveStatus() {
        when(storageInterface.fetchValueForKey(anyString(), eq(secureStorageError))).thenReturn(storedValueHighVersion);
        handler.fetchConsentStates(getTestConfigurationList(), new TestCheckConsentCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                assertEquals(ConsentStatus.active, consents.get(0).getStatus());
            }
        });
    }

    @Test
    public void verifyPostSuccess() {
        when(storageInterface.storeValueForKey(anyString(), anyString(), eq(secureStorageError))).thenReturn(true).thenReturn(true);
        handler.storeConsentState(consentDefinition, true, new TestPostConsentCallback() {
            @Override
            public void onPostConsentSuccess(Consent consent) {
                assertEquals(ConsentStatus.active, consent.getStatus());
            }
        });
    }

    @Test
    public void verifyPostFailure() {
        when(storageInterface.storeValueForKey(anyString(), anyString(), eq(secureStorageError))).thenReturn(false);
        when(secureStorageError.getErrorCode()).thenReturn(SecureStorageInterface.SecureStorageError.secureStorageError.AccessKeyFailure);
        handler.storeConsentState(consentDefinition, true, new TestPostConsentCallback() {
            @Override
            public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
                assertTrue(error.getError().startsWith("Error updating device stored consent"));
            }
        });
    }

    @NonNull
    private ArrayList<String> getConsentTypes() {
        ArrayList<String> types = new ArrayList<>();
        types.add("type1");
        types.add("type2");
        return types;
    }

    private List<ConsentDefinition> getTestConfigurationList() {
        List<ConsentDefinition> definitions = new ArrayList<>();
        definitions.add(consentDefinition);
        return definitions;
    }

    private class TestDeviceStoredConsentHandler extends DeviceStoredConsentHandler {

        TestDeviceStoredConsentHandler(AppInfra appInfra) {
            super(appInfra);
        }

        @NonNull
        @Override
        SecureStorageInterface.SecureStorageError getSecureStorageError() {
            return secureStorageError;
        }

        @Override
        long getUTCTime() {
            return System.currentTimeMillis();
        }
    }

    private class TestPostConsentCallback implements PostConsentCallback {

        @Override
        public void onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
            throw new RuntimeException("onPostConsentFailed Failed");
        }

        @Override
        public void onPostConsentSuccess(Consent consent) {
            throw new RuntimeException("onPostConsentSuccess Failed");
        }
    }

    private class TestCheckConsentCallback implements CheckConsentsCallback {

        @Override
        public void onGetConsentsSuccess(List<Consent> consents) {
            throw new RuntimeException("onGetConsentsSuccess Failed");
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
            throw new RuntimeException("onGetConsentsFailed Failed");
        }
    }
}