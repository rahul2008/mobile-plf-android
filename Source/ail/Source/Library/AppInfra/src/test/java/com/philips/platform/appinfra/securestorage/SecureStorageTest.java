package com.philips.platform.appinfra.securestorage;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestoragev1.SecureStorageV1;
import com.philips.platform.appinfra.securestoragev2.SecureStorageV2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by abhishek on 4/5/18.
 */

@RunWith(MockitoJUnitRunner.class)
public class SecureStorageTest {

    @Mock
    static ArrayList<SecureStorageInterface> allSecureStorageListMock;


    static ArrayList<SecureStorageInterface> oldSecureStorageListMock;

    SecureStorageInterface secureStorageInterface;

    @Mock
    static SecureStorageInterface latestSecureStorageMock;

    @Mock
    static SecureStorageInterface secureStorageV1;


    @Mock
    AppInfra appInfra;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    SecretKey secretKey;

    @Mock
    SecureStorageInterface oldSecureStorageMock;

    @Mock
    static ByteBuffer byteBuffer;

    @Mock
    SecureStorageInterface.DataEncryptionListener dataEncryptionListener;

    @Mock
    SecureStorageInterface.DataDecryptionListener dataDecryptionListener;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfra.getAppInfraLogInstance()).thenReturn(loggingInterface);
        secureStorageInterface=new SecureStorageMock(appInfra);
    }


    @Test
    public void storeValueForKey_PresentIn_Old_SS() throws Exception {
       SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected boolean doesKeyExistInOldSS(String key) {
                return true;
            }

           @Override
           protected String migrateDataFromOldToNewSS(String userKey, SecureStorageError secureStorageError) {
               return "user_data";
           }
       };
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        when(latestSecureStorageMock.storeValueForKey(anyString(),anyString(),any(SecureStorageInterface.SecureStorageError.class))).thenReturn(true);
        secureStorage.storeValueForKey("user_key","user_data",secureStorageError);
        verify(latestSecureStorageMock).storeValueForKey("user_key","user_data",secureStorageError);
        assertTrue(secureStorage.storeValueForKey("user_key","user_data",secureStorageError));
    }

    @Test
    public void fetchValueForKey_Absent_In_Latest_SS() throws Exception {
        when(latestSecureStorageMock.doesStorageKeyExist("user_key")).thenReturn(false);
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected boolean doesKeyExistInOldSS(String key) {
                return true;
            }

            @Override
            protected String migrateDataFromOldToNewSS(String userKey, SecureStorageError secureStorageError) {
                return "user_data";
            }
        };
        assertEquals("user_data",secureStorage.fetchValueForKey("user_key",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void fetchValueForKey_Absent_In_All_SS() throws Exception {
        when(latestSecureStorageMock.doesStorageKeyExist("user_key")).thenReturn(false);
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected boolean doesKeyExistInOldSS(String key) {
                return false;
            }

        };
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        secureStorage.fetchValueForKey("user_key",secureStorageError);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey,secureStorageError.getErrorCode());
    }

    @Test
    public void fetchValueForKey_Present_In_Latest_SS() throws Exception {
        when(latestSecureStorageMock.doesStorageKeyExist("user_key")).thenReturn(true);
        when(latestSecureStorageMock.fetchValueForKey(anyString(),any(SecureStorageInterface.SecureStorageError.class))).thenReturn("user_data");
        assertEquals("user_data",secureStorageInterface.fetchValueForKey("user_key",new SecureStorageInterface.SecureStorageError()));
    }



    @Test
    public void createKey_SecureKey_Present_In_Old_SS() throws Exception {
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected boolean doesSecureKeyExistInOldSS(String key) {
                return true;
            }
        };
        assertFalse(secureStorage.createKey(SecureStorageInterface.KeyTypes.AES,"user_key",secureStorageError));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.SecureKeyAlreadyPresent,secureStorageError.getErrorCode());
    }

    @Test
    public void createKey_SecureKey_Absent_In_Old_SS() throws Exception {
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected boolean doesSecureKeyExistInOldSS(String key) {
                return false;
            }
        };
        when(latestSecureStorageMock.createKey(any(SecureStorageInterface.KeyTypes.class),anyString(),any(SecureStorageInterface.SecureStorageError.class))).thenReturn(true);
        assertTrue(secureStorage.createKey(SecureStorageInterface.KeyTypes.AES,"user_key",secureStorageError));
    }


    @Test
    public void getKey_Exist_In_New_SS() throws Exception {
        when(latestSecureStorageMock.doesEncryptionKeyExist(anyString())).thenReturn(true);
        when(latestSecureStorageMock.getKey(anyString(),any(SecureStorageInterface.SecureStorageError.class))).thenReturn(secretKey);
        assertNotNull(secureStorageInterface.getKey("user_key",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void getKey_Present_In_Old_SS() throws Exception {
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected SecureStorageInterface getOldSSInstanceWhereSecureKeyIsAvailable(String key) {
                return oldSecureStorageMock;
            }
        };
        when(latestSecureStorageMock.doesEncryptionKeyExist(anyString())).thenReturn(false);
        when(oldSecureStorageMock.getKey(anyString(),any(SecureStorageInterface.SecureStorageError.class))).thenReturn(secretKey);
        assertNotNull(secureStorage.getKey("user_key",secureStorageError));
    }

    @Test
    public void getKey_Not_Present_In_SS() throws Exception {
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected SecureStorageInterface getOldSSInstanceWhereSecureKeyIsAvailable(String key) {
                return null;
            }
        };
        when(latestSecureStorageMock.doesEncryptionKeyExist(anyString())).thenReturn(false);
        assertNull(secureStorage.getKey("user_key",secureStorageError));
    }


    @Test
    public void encryptData() throws Exception {
        secureStorageInterface.encryptBulkData(new byte[0], dataEncryptionListener);
        verify(latestSecureStorageMock).encryptBulkData(any(byte[].class),any(SecureStorageInterface.DataEncryptionListener.class));
    }

    @Test
    public void encryptBulkData() throws Exception {
        secureStorageInterface.encryptData(new byte[0],new SecureStorageInterface.SecureStorageError());
        verify(latestSecureStorageMock).encryptData(any(byte[].class),any(SecureStorageInterface.SecureStorageError.class));
    }

    @Test
    public void decryptData_Null_Data() throws Exception {
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        secureStorageInterface.decryptData(null,secureStorageError);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.NullData,secureStorageError.getErrorCode());
    }

    @Test
    public void decryptDataTest_From_SS1() throws Exception {
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        when(byteBuffer.getInt()).thenReturn(2);
        secureStorageInterface.decryptData(new byte[0],secureStorageError);
        verify(secureStorageV1).decryptData(any(byte[].class),any(SecureStorageInterface.SecureStorageError.class));
    }


    @Test
    public void decryptBulkDataTest_From_SS1() throws Exception {
        when(byteBuffer.getInt()).thenReturn(2);
        secureStorageInterface.decryptBulkData(new byte[0], dataDecryptionListener);
        verify(secureStorageV1).decryptBulkData(any(byte[].class),any(SecureStorageInterface.DataDecryptionListener.class));
    }

    @Test
    public void migrateDataFromOldToNewSSTest() throws Exception {
        when(latestSecureStorageMock.doesStorageKeyExist("user_key")).thenReturn(false);
        when(latestSecureStorageMock.storeValueForKey(anyString(),anyString(),any(SecureStorageInterface.SecureStorageError.class))).thenReturn(true);
        when(secureStorageV1.fetchValueForKey(anyString(),any(SecureStorageInterface.SecureStorageError.class))).thenReturn("user_data");
        when(secureStorageV1.removeValueForKey(anyString())).thenReturn(true);
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected boolean doesKeyExistInOldSS(String key) {
                return true;
            }

            @Override
            protected SecureStorageInterface getOldSSInstanceWhereKeyIsAvailable(String key) {
                return secureStorageV1;
            }
        };
        assertEquals("user_data",secureStorage.fetchValueForKey("user_key",new SecureStorageInterface.SecureStorageError()));

    }

    @Test
    public void migrateDataFromOldToNewSSTest_Migration_Failure() throws Exception {
        SecureStorageInterface.SecureStorageError secureStorageError=new SecureStorageInterface.SecureStorageError();
        when(latestSecureStorageMock.doesStorageKeyExist("user_key")).thenReturn(false);
        SecureStorageInterface secureStorage= new SecureStorageMock(appInfra){
            @Override
            protected boolean doesKeyExistInOldSS(String key) {
                return true;
            }

            @Override
            protected SecureStorageInterface getOldSSInstanceWhereKeyIsAvailable(String key) {
                return null;
            }
        };
        secureStorage.fetchValueForKey("user_key",secureStorageError);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey,secureStorageError.getErrorCode());

    }



    static class SecureStorageMock extends  SecureStorage{

        public SecureStorageMock(AppInfraInterface bAppInfra) {
            super(bAppInfra);
        }

        @Override
        protected ArrayList<SecureStorageInterface> getAllSecureStorageList() {
            return allSecureStorageListMock;
        }

        @Override
        protected ArrayList<SecureStorageInterface> getListOfOldSecureStorage() {
            return oldSecureStorageListMock;
        }

        @NonNull
        @Override
        protected SecureStorageInterface getLatestSecureStorage() {
            return latestSecureStorageMock;
        }

        @Override
        protected SecureStorageInterface getOldSSInstanceWhereKeyIsAvailable(String key) {
            return secureStorageV1;
        }

        @Override
        protected SecureStorageInterface getSecureStorage(String version) {
            switch(version){
                case SecureStorageV2.VERSION:
                    return latestSecureStorageMock;
                case SecureStorageV1.VERSION:
                default:
                    return secureStorageV1;
            }
        }

        @Override
        protected ByteBuffer getWrappedByteArray(byte[] dataToBeWrapped) {
            return byteBuffer;
        }
    }

}