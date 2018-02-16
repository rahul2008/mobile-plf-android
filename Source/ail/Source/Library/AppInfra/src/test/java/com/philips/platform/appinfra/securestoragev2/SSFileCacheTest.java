package com.philips.platform.appinfra.securestoragev2;

import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.AppInfraLogging;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

/**
 * Created by abhishek on 2/6/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class SSFileCacheTest {
    @Mock
    static SharedPreferences sharedPreferences;

    @Mock
    SharedPreferences.Editor editor;

    SSFileCache ssFileCache;

    @Mock
    AppInfra appInfra;

    @Mock
    AppInfraLogging appInfraLogging;



    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        when(appInfra.getAppInfraLogInstance()).thenReturn(appInfraLogging);
        when(sharedPreferences.edit()).thenReturn(editor);
        ssFileCache=new SSFileCacheMock(appInfra);
    }

    @Test
    public void testRSAWrappedAESKeyInFileCache(){
        ssFileCache.putRSAWrappedAESKeyInFileCache("key","value");
        when(sharedPreferences.getString(anyString(),anyString())).thenReturn("value");
        assertEquals("value",ssFileCache.getRSAWrappedAESKeyFromFileCache("key"));
    }

    @Test
    public void testEncryptedStringInFileCache(){
        ssFileCache.putEncryptedString("key","value");
        when(sharedPreferences.getString(anyString(),anyString())).thenReturn("value");
        assertEquals("value",ssFileCache.getEncryptedString("key"));
    }

    @Test
    public void testDeleteKey_Should_Return_True(){
        when(sharedPreferences.contains(anyString())).thenReturn(true);
        when(editor.commit()).thenReturn(true);
        assertTrue(ssFileCache.deleteKey("key"));
    }

    @Test
    public void testDeleteKey_Should_throw_exception(){
        when(sharedPreferences.contains(anyString())).thenReturn(true);
        when(editor.commit()).thenThrow(new IllegalArgumentException());
        assertFalse(ssFileCache.deleteKey("key"));
    }

    @Test
    public void testDeleteEncryptedData_Should_return_false(){
        when(sharedPreferences.contains(anyString())).thenReturn(false);
        when(editor.commit()).thenReturn(true);
        assertFalse(ssFileCache.deleteEncryptedData("key"));
    }

    @Test
    public void testDeleteEncryptedData_Should_Return_True(){
        when(sharedPreferences.contains(anyString())).thenReturn(true);
        when(editor.commit()).thenReturn(true);
        assertTrue(ssFileCache.deleteEncryptedData("key"));
    }

    @Test
    public void testDeleteEncryptedData_Should_throw_exception(){
        when(sharedPreferences.contains(anyString())).thenReturn(true);
        when(editor.commit()).thenThrow(new IllegalArgumentException());
        assertFalse(ssFileCache.deleteEncryptedData("key"));
    }

    @Test
    public void testDeleteKey_Should_return_false(){
        when(sharedPreferences.contains(anyString())).thenReturn(false);
        when(editor.commit()).thenReturn(true);
        assertFalse(ssFileCache.deleteKey("key"));
    }
    static class SSFileCacheMock extends SSFileCache{

        public SSFileCacheMock(AppInfra appInfra) {
            super(appInfra);
        }

        @Override
        protected SharedPreferences getSharedPreferences(String fileName) {
            return sharedPreferences;
        }
    }
}