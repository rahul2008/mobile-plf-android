package com.philips.platform.appinfra.securestoragev2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.Random;

import javax.crypto.SecretKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by abhishek on 1/24/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class SecureStorage2Test {

    SecureStorageInterface mSecureStorage ;

    @Mock
    AppInfra appInfra;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    Context context;

    @Mock
    SharedPreferences sharedPreferences;

    @Mock
    static SSFileCache ssFileCache;

    @Mock
    static SSEncoderDecoder SSEncoderDecoder;

    @Mock
    ApplicationInfo applicationInfo;

    @Mock
    Key key;

    @Mock
    static SSKeyProvider ssKeyProvider;

    @Mock
    SecretKey secretKey;

    @Mock
    SecureStorageInterface.DataEncryptionListener dataEncryptionListener;

    @Mock
    SecureStorageInterface.DataDecryptionListener dataDecryptionListener;

    @Mock
    static HandlerThread handlerThread;

    @Mock
    static Handler handler;




    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfra.getAppInfraContext()).thenReturn(context);
        when(appInfra.getAppInfraLogInstance()).thenReturn(loggingInterface);
        when(context.getApplicationInfo()).thenReturn(applicationInfo);
        when(handler.post(any(Runnable.class))).thenAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0,Runnable.class);
                runnable.run();
                return null;
            }
        });
        mSecureStorage=new SecureStorage2Mock(appInfra);
    }


    @Test
    public void testForEmptyKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.storeValueForKey("", "value", sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey,sse.getErrorCode());
    }

    @Test
    public void testForNullKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.storeValueForKey(null, null, sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey,sse.getErrorCode());
    }



    public String generateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }


    @Test
    public void testForSpaceKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.storeValueForKey(" ", "val", sse)); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey("   ", "val", sse)); // value can be empty
    }

    @Test
    public void testStoreValueForKey_Should_Return_True() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(String.class))).thenReturn("asbjds");
        when(ssFileCache.putEncryptedString(any(String.class),any(String.class))).thenReturn(true);
        assertTrue (mSecureStorage.storeValueForKey("key1","value1",sse));

    }

    @Test
    public void testStoreValueForKey_KeyProvider_Exception() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenThrow(new SSKeyProviderException(""));;
        assertFalse(mSecureStorage.storeValueForKey("key1","value1",sse));

    }

    @Test
    public void testStoreValueForKey_EncodeDecode_Exception() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(String.class))).thenThrow(new SSEncodeDecodeException(""));
        assertFalse(mSecureStorage.storeValueForKey("key1","value1",sse));

    }

    @Test
    public void testStoreValueForKey_Exception() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(String.class))).thenThrow(new IllegalArgumentException(""));
        assertFalse(mSecureStorage.storeValueForKey("key1","value1",sse));

    }

    @Test
    public void testFetchValueForNullKey() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.fetchValueForKey(null, sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
        assertNull(mSecureStorage.fetchValueForKey("NotSavedKey", sse));
    }


    @Test
    public void testFetchValueForEmptyKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.fetchValueForKey("", sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testFetchValueForKey_Positive_Scenario() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(String.class))).thenReturn("value");
        when(ssFileCache.getEncryptedString(any(String.class))).thenReturn("ashdjsjas");
        assertEquals("value",mSecureStorage.fetchValueForKey("key1",sse));
    }


    @Test
    public void testFetchValueForKey_KeyProvider_Exception() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenThrow(new SSKeyProviderException(""));;
        assertNull(mSecureStorage.fetchValueForKey("key1",sse));

    }

    @Test
    public void testFetchValueForKey_EncodeDecode_Exception() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(String.class))).thenThrow(new SSEncodeDecodeException(""));
        when(ssFileCache.getEncryptedString(any(String.class))).thenReturn("ashdjsjas");
        assertNull(mSecureStorage.fetchValueForKey("key1",sse));

    }

    @Test
    public void testFetchValueForKey_Exception() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(String.class))).thenThrow(new IllegalArgumentException(""));
        when(ssFileCache.getEncryptedString(any(String.class))).thenReturn("ashdjsjas");
        assertNull(mSecureStorage.fetchValueForKey("key1",sse));

    }

    private String getLargeString() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("big_string");
        File file = new File(resource.getPath());
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }

    @Test
    public void removeValueForKey_userKey_NULL_Should_return_false(){
        assertFalse(mSecureStorage.removeValueForKey(null));
    }

    @Test
    public void removeValueForKey_Should_Return_True(){
        when(ssFileCache.deleteEncryptedData(any(String.class))).thenReturn(true);
        assertTrue(mSecureStorage.removeValueForKey("abcd"));
    }

    @Test
    public void createKeyTest_Should_Return_True() throws Exception{
        when(ssKeyProvider.getSecureKey("abcd")).thenReturn(secretKey);
        assertTrue(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES,"abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void createKeyTest_Should_Return_False_Key_Provider_Exception() throws Exception{
        when(ssKeyProvider.getSecureKey("abcd")).thenThrow(new SSKeyProviderException(""));
        assertFalse(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES,"abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void createKeyTest_Should_Return_False_IllegalArgumentException() throws Exception{
        when(ssKeyProvider.getSecureKey("abcd")).thenThrow(new IllegalArgumentException());
        assertFalse(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES,"abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void getKeyTest_Should_Return_Key() throws Exception{
        when(ssKeyProvider.getSecureKey("abcd")).thenReturn(secretKey);
        assertNotNull(mSecureStorage.getKey("abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void getKeyTest_Should_Return_Null_Key_Provider_Exception() throws Exception{
        when(ssKeyProvider.getSecureKey("abcd")).thenThrow(new SSKeyProviderException(""));
        assertNull(mSecureStorage.getKey("abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void getKeyTest_Should_Return_Null_IllegalArgumentException() throws Exception{
        when(ssKeyProvider.getSecureKey("abcd")).thenThrow(new IllegalArgumentException());
        assertNull(mSecureStorage.getKey("abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void testClearKey_Should_return_false_empty_key(){
        assertFalse(mSecureStorage.clearKey("",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void testClearKey_Should_return_false(){
        assertFalse(mSecureStorage.clearKey("abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void testClearKey_Should_return_true(){
        when(ssFileCache.deleteKey(any(String.class))).thenReturn(true);
        assertTrue(mSecureStorage.clearKey("abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void testClearKey_Should_return_false_exception_case(){
        when(ssFileCache.deleteKey(any(String.class))).thenThrow(new IllegalArgumentException());
        assertFalse(mSecureStorage.clearKey("abcd",new SecureStorageInterface.SecureStorageError()));
    }

    @Test
    public void testGetDeviceCapability() {
        assertNotNull(mSecureStorage.getDeviceCapability());
    }


    @Test
    public void testByteDataEncryptionForNullValue() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.encryptData(null, sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.NullData, sse.getErrorCode());
    }

    @Test
    public void testByteDataEncryption_Should_Not_Be_Null() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        byte[] encryptedBytes=new String("dcba").getBytes();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenReturn(encryptedBytes);
        assertNotNull(mSecureStorage.encryptData(new String("abcd").getBytes(), sse));
    }

    @Test
    public void testByteDataEncryption_Should_Return_AccessKeyFailure_Key_Provider_Exception() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenThrow(new SSKeyProviderException(""));
        mSecureStorage.encryptData(new String("abcd").getBytes(), sse);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.AccessKeyFailure,sse.getErrorCode());
    }

    @Test
    public void testByteDataEncryption_Should_Return_EncryptionError_Encode_Decode_exception() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenThrow(new SSEncodeDecodeException(""));
        mSecureStorage.encryptData(new String("abcd").getBytes(), sse);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.EncryptionError,sse.getErrorCode());
    }

    @Test
    public void testByteDataEncryption_Should_return_Encryption_Error_Exception() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenThrow(new IllegalArgumentException(""));
        mSecureStorage.encryptData(new String("abcd").getBytes(), sse);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.EncryptionError,sse.getErrorCode());
    }

    @Test
    public void testByteDataDecryptionForNullValue() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.decryptData(null, sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.NullData, sse.getErrorCode());
    }

    @Test
    public void testByteDataDecryption_Should_Not_Be_Null() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        byte[] encryptedBytes=new String("dcba").getBytes();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenReturn(encryptedBytes);
        assertNotNull(mSecureStorage.decryptData(new String("abcd").getBytes(), sse));
    }

    @Test
    public void testByteDataDecryption_Should_Return_AccessKeyFailure_Key_Provider_Exception() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenThrow(new SSKeyProviderException(""));
        mSecureStorage.decryptData(new String("abcd").getBytes(), sse);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.AccessKeyFailure,sse.getErrorCode());
    }

    @Test
    public void testByteDataDecryption_Should_Return_EncryptionError_Encode_Decode_exception() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenThrow(new SSEncodeDecodeException(""));
        mSecureStorage.decryptData(new String("abcd").getBytes(), sse);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.DecryptionError,sse.getErrorCode());
    }

    @Test
    public void testByteDataDecryption_Should_return_Encryption_Error_Exception() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenThrow(new IllegalArgumentException(""));
        mSecureStorage.decryptData(new String("abcd").getBytes(), sse);
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.DecryptionError,sse.getErrorCode());
    }

    @Test
    public void testEncryptAsyncBulkData() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        byte[] encryptedBytes=new String("dcba").getBytes();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenReturn(encryptedBytes);
        mSecureStorage.encryptBulkData(new String("abcd").getBytes(),dataEncryptionListener);
        verify(dataEncryptionListener).onEncryptionSuccess(any(byte[].class));
    }

    @Test
    public void testEncryptAsyncBulkData_Error_Scenario() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenThrow(new SSKeyProviderException(""));
        mSecureStorage.encryptBulkData(new String("abcd").getBytes(),dataEncryptionListener);
        verify(dataEncryptionListener).onEncryptionError(any(SecureStorageInterface.SecureStorageError.class));
    }

    @Test
    public void testAsyncDecryptBulkData() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        byte[] encryptedBytes=new String("dcba").getBytes();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenReturn(secretKey);
        when(SSEncoderDecoder.encodeDecodeData(anyInt(),any(Key.class),any(byte[].class))).thenReturn(encryptedBytes);
        mSecureStorage.decryptBulkData(new String("abcd").getBytes(),dataDecryptionListener);
        verify(dataDecryptionListener).onDecryptionSuccess(any(byte[].class));
    }

    @Test
    public void testAsyncDecryptBulkData_Error_Scenario() throws Exception{
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        when(ssKeyProvider.getSecureKey(any(String.class))).thenThrow(new SSKeyProviderException(""));
        mSecureStorage.decryptBulkData(new String("abcd").getBytes(),dataDecryptionListener);
        verify(dataDecryptionListener).onDecyptionError(any(SecureStorageInterface.SecureStorageError.class));
    }

    public static class SecureStorage2Mock extends SecureStorage2 {

        public SecureStorage2Mock(AppInfra bAppInfra) {
            super(bAppInfra);
        }

        @NonNull
        @Override
        protected SSEncoderDecoder getSSEncoderDecoder() {
            return SSEncoderDecoder;
        }

        @Override
        protected SSFileCache getSecureStorageFileCahce() {
            return ssFileCache;
        }

        @Override
        protected SSKeyProvider getSecureStorageKeyprovider() {
            return ssKeyProvider;
        }

        @NonNull
        @Override
        protected Handler getHandler(Looper looper) {
            return handler;
        }

        @NonNull
        @Override
        protected HandlerThread getWorkerThread() {
            return handlerThread;
        }
    }
}