package com.philips.platform.appframework.abtesting;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbTestingLocalCacheTest {

    private AbTestingLocalCache abTestingLocalCache;
    @Mock
    private Context contextMock;
    @Mock
    private SharedPreferences sharedPreferencesMock;
    @Mock
    private SharedPreferences.Editor editorMock;
    @Mock
    private AppInfraInterface appInfraInterfaceMock;
    @Mock
    private LoggingInterface loggingInterfaceMock;

    private String testValue = "{\"mTestValues\":{\"key1\":{\"testValue\":\"some_value\",\"updateType\":\"ONLY_AT_APP_UPDATE\",\"appVersion\":\"some_version\"}}}";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterfaceMock);
        when(contextMock.getSharedPreferences("abTest_preference",Context.MODE_PRIVATE)).thenReturn(sharedPreferencesMock);
        when(sharedPreferencesMock.edit()).thenReturn(editorMock);
        when(sharedPreferencesMock.getString("abtestcachedobj",null)).thenReturn(testValue);
        Gson gson = new Gson();
        abTestingLocalCache = new AbTestingLocalCache(contextMock, gson);
        abTestingLocalCache.initAppInfra(appInfraInterfaceMock);
    }

    @Test
    public void shouldSaveToDisk() {
        abTestingLocalCache.saveCacheToDisk();
        verify(editorMock).putString("abtestcachedobj", testValue);
        verify(editorMock).apply();
    }

    @Test
    public void shouldFetchFromDisk() {
        assertNotNull(abTestingLocalCache.fetchFromDisk());
    }

    @Test
    public void shouldRemoveTestValue() {
        abTestingLocalCache.removeFromDisk("key1");
        assertTrue(abTestingLocalCache.getPreferenceCacheModel().getTestValues().size()==0);
    }

    @Test
    public void shouldUpdateTestValues() {
        Map<String,CacheModel.ValueModel> map = new HashMap<>();
        CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        map.put("some_key", valueModel);
        abTestingLocalCache.updatePreferenceCacheModel(map);
        assertTrue(abTestingLocalCache.getPreferenceCacheModel().getTestValues().equals(map));
        assertEquals(map.get("some_key"),valueModel);
    }


}