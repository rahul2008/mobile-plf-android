package com.philips.platform.dscdemo.database;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.aikm.AIKMInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.appupdate.AppUpdateInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.consenthandler.DeviceStoredConsentHandler;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

import java.security.Key;

class AppInfraStub implements AppInfraInterface {

    public LoggingInterfaceStub loggingInterfaceStub = new LoggingInterfaceStub();

    @Override
    public SecureStorageInterface getSecureStorage() {
        return new SecureStorageInterface() {
            @Override
            public boolean storeValueForKey(String userKey, String valueToBeEncrypted, SecureStorageError secureStorageError) {
                return false;
            }

            @Override
            public boolean storeValueForKey(String userKey, byte[] valueToBeEncrypted, SecureStorageError secureStorageError) {
                return false;
            }

            @Override
            public String fetchValueForKey(String userKey, SecureStorageError secureStorageError) {
                return null;
            }

            @Override
            public byte[] fetchByteArrayForKey(String userKey, SecureStorageError secureStorageError) {
                return new byte[0];
            }

            @Override
            public boolean removeValueForKey(String userKey) {
                return false;
            }

            @Override
            public boolean createKey(KeyTypes keyType, String keyName, SecureStorageError error) {
                return false;
            }

            @Override
            public boolean doesStorageKeyExist(String key) {
                return false;
            }

            @Override
            public boolean doesEncryptionKeyExist(String key) {
                return true;
            }

            @Override
            public Key getKey(String keyName, SecureStorageError error) {
                return null;
            }

            @Override
            public boolean clearKey(String keyName, SecureStorageError error) {
                return false;
            }

            @Override
            public byte[] encryptData(byte[] dataToBeEncrypted, SecureStorageError secureStorageError) {
                return new byte[0];
            }

            @Override
            public void encryptBulkData(byte[] dataToBeEncrypted, DataEncryptionListener dataEncryptionListener) {

            }

            @Override
            public byte[] decryptData(byte[] dataToBeDecrypted, SecureStorageError secureStorageError) {
                return new byte[0];
            }

            @Override
            public void decryptBulkData(byte[] dataToBeDecrypted, DataDecryptionListener dataDecryptionListener) {

            }

            @Override
            public boolean isCodeTampered() {
                return false;
            }

            @Override
            public boolean isEmulator() {
                return false;
            }

            @Override
            public String getDeviceCapability() {
                return null;
            }

            @Override
            public boolean deviceHasPasscode() {
                return false;
            }
        };
    }

    @Override
    public AppIdentityInterface getAppIdentity() {
        return new AppIdentityInterface() {
            @Override
            public String getAppName() {
                return null;
            }

            @Override
            public String getAppVersion() {
                return null;
            }

            @Override
            public AppState getAppState() {
                return null;
            }

            @Override
            public String getLocalizedAppName() {
                return null;
            }

            @Override
            public String getMicrositeId() {
                return null;
            }

            @Override
            public String getSector() {
                return null;
            }

            @Override
            public String getServiceDiscoveryEnvironment() {
                return null;
            }
        };
    }

    @Override
    public InternationalizationInterface getInternationalization() {
        return null;
    }

    @Override
    public LoggingInterface getLogging() {
        return loggingInterfaceStub;
    }

    @Override
    public ServiceDiscoveryInterface getServiceDiscovery() {
        return null;
    }

    @Override
    public AppTaggingInterface getTagging() {
        return null;
    }

    @Override
    public TimeInterface getTime() {
        return null;
    }

    @Override
    public AppConfigurationInterface getConfigInterface() {
        return null;
    }

    @Override
    public RestInterface getRestClient() {
        return null;
    }

    @Override
    public ABTestClientInterface getAbTesting() {
        return null;
    }

    @Override
    public LanguagePackInterface getLanguagePack() {
        return null;
    }

    @Override
    public AppUpdateInterface getAppUpdate() {
        return null;
    }

    @Override
    public AIKMInterface getAiKmInterface() {
        return null;
    }

    @Override
    public ConsentManagerInterface getConsentManager() {
        return null;
    }

    @Override
    public DeviceStoredConsentHandler getDeviceStoredConsentHandler() {
        return null;
    }
}
