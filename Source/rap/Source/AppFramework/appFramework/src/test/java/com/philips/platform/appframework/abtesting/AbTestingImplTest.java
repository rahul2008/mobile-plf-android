package com.philips.platform.appframework.abtesting;


import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbTestingImplTest {

    private AbTestingImpl abTesting;
    @Mock
    private AppInfraInterface appInfraInterfaceMock;
    @Mock
    private AbTestingLocalCache abTestingLocalCacheMock;
    @Mock
    private FireBaseWrapper fireBaseWrapperMock;
    @Mock
    private LoggingInterface loggingInterfaceMock;
    @Mock
    private CacheModel cacheModelMock;
    private Map<String, CacheModel.ValueModel> dummyData = new HashMap<>();
    @Mock
    private AppIdentityInterface appIdentityInterfaceMock;
    @Mock
    private ABTestClientInterface.OnRefreshListener onRefreshListenerMock;
    @Mock
    private RestInterface restInterfaceMock;
    @Mock
    private FetchDataHandler fetchDataHandlerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dummyData.put("key1", getValueModel());
        dummyData.put("key2", getValueModel2());

        abTesting = new AbTestingImpl() {
            @NonNull
            @Override
            AbTestingLocalCache getAbTestingLocalCache(AppInfraInterface appInfraInterface) {
                return abTestingLocalCacheMock;
            }

            @NonNull
            @Override
            FireBaseWrapper getFireBaseWrapper() {
                return fireBaseWrapperMock;
            }

            @NonNull
            @Override
            FetchDataHandler getFetchDataHandler() {
                return fetchDataHandlerMock;
            }
        };
        when(appIdentityInterfaceMock.getAppVersion()).thenReturn("18.3");
        when(appInfraInterfaceMock.getAppIdentity()).thenReturn(appIdentityInterfaceMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterfaceMock);
        when(abTestingLocalCacheMock.getCacheFromPreference()).thenReturn(cacheModelMock);
        abTesting.initFireBase();
    }


    @Test
    public void shouldInitAbTesting() {
        when(cacheModelMock.getTestValues()).thenReturn(dummyData);
        abTesting.initAbTesting(appInfraInterfaceMock);
        verify(abTestingLocalCacheMock).initAppInfra(appInfraInterfaceMock);
        verify(fireBaseWrapperMock).initAppInfra(appInfraInterfaceMock);
        assertTrue(abTesting.getInMemoryCache().size() == 2);
    }

    @Test
    public void shouldReturnTestValueAsExpected() {
        when(cacheModelMock.getTestValues()).thenReturn(dummyData);
        abTesting.initAbTesting(appInfraInterfaceMock);
        assertNull(abTesting.getTestValue("", "", null));
        String testValue = abTesting.getTestValue("key1", "default_value", ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE);
        assertTrue(testValue.equals("value1"));
        assertEquals(abTesting.getInMemoryCache().get("key1").getUpdateType(), ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name());
        assertEquals(abTesting.getInMemoryCache().get("key1").getAppVersion(), "18.3");
        verify(abTestingLocalCacheMock).updatePreferenceCacheModel(abTesting.getInMemoryCache());
        verify(abTestingLocalCacheMock).saveCacheToDisk();
        testValue = abTesting.getTestValue("key3", "default_value", ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE);
        assertTrue(testValue.equals("default_value"));

        String testValue2 = abTesting.getTestValue("key2", "default_value", ABTestClientInterface.UPDATETYPES.EVERY_APP_START);
        assertTrue(testValue2.equals("value2"));
        assertEquals(abTesting.getInMemoryCache().get("key2").getUpdateType(), ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name());
        verify(abTestingLocalCacheMock).removeFromDisk("key2");

    }

    @Test
    public void shouldEnableDeveloperMode() {
        abTesting.enableDeveloperMode(true);
        verify(fireBaseWrapperMock).enableDeveloperMode(true);
    }

    @Test
    public void shouldReturnCacheStatusNotUpdatedByDefault() {
        assertTrue(abTesting.getCacheStatus() == ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_NOT_UPDATED);
    }

    @Test
    public void shouldUpdateCacheWhenAsked() {
        when(appInfraInterfaceMock.getRestClient()).thenReturn(restInterfaceMock);
        when(restInterfaceMock.isInternetReachable()).thenReturn(false);
        abTesting.initAbTesting(appInfraInterfaceMock);
        abTesting.updateCache(onRefreshListenerMock);
        verify(onRefreshListenerMock).onError(ABTestClientInterface.OnRefreshListener.ERRORVALUES.NO_NETWORK);
        when(restInterfaceMock.isInternetReachable()).thenReturn(true);
        abTesting.updateCache(onRefreshListenerMock);
        verify(fireBaseWrapperMock).fetchDataFromFireBase(fetchDataHandlerMock, onRefreshListenerMock);
    }

    @Test
    public void shouldSyncInMemoryCacheAsExpected() {
        abTesting = new AbTestingImpl() {
            @NonNull
            @Override
            AbTestingLocalCache getAbTestingLocalCache(AppInfraInterface appInfraInterface) {
                return abTestingLocalCacheMock;
            }

            @NonNull
            @Override
            FireBaseWrapper getFireBaseWrapper() {
                return fireBaseWrapperMock;
            }
        };
        dummyData.put("key1", getValueModel2());
        CacheModel.ValueModel valueModel = getValueModel();
        dummyData.put("key2", valueModel);

        when(cacheModelMock.getTestValues()).thenReturn(dummyData);
        abTesting.initFireBase();
        abTesting.initAbTesting(appInfraInterfaceMock);

        Map<String, CacheModel.ValueModel> dummyData2 = new HashMap<>();
        dummyData2.put("key1", getValueModel2());
        dummyData2.put("key2", getValueModel());
        dummyData2.get("key2").setTestValue("new_value");
        abTesting.getFetchDataHandler().fetchData(dummyData2);
        assertEquals(abTesting.getInMemoryCache().get("key1").getTestValue(), "value2");
        assertEquals(abTesting.getInMemoryCache().get("key1").getAppVersion(), "18.3");
        assertEquals(abTesting.getInMemoryCache().get("key1").getUpdateType(), ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name());

        assertEquals(abTesting.getInMemoryCache().get("key2").getTestValue(), "value1");
        assertEquals(abTesting.getInMemoryCache().get("key2").getAppVersion(), "18.3");
        assertEquals(abTesting.getInMemoryCache().get("key2").getUpdateType(), ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name());

        when(appIdentityInterfaceMock.getAppVersion()).thenReturn("18.4");
        dummyData2.get("key2").setAppVersion("18.4");
        abTesting.getFetchDataHandler().fetchData(dummyData2);
        assertEquals(abTesting.getInMemoryCache().get("key2").getTestValue(), "new_value");
        assertEquals(abTesting.getInMemoryCache().get("key2").getAppVersion(), "18.4");

        abTesting.getFetchDataHandler().updateCacheStatus(ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_UPDATED);
        assertTrue(abTesting.getCacheStatus().equals(ABTestClientInterface.CACHESTATUSVALUES.EXPERIENCE_UPDATED));
    }

    private CacheModel.ValueModel getValueModel() {
        CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setUpdateType(ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE.name());
        valueModel.setTestValue("value1");
        valueModel.setAppVersion("18.3");
        return valueModel;
    }

    private CacheModel.ValueModel getValueModel2() {
        CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setUpdateType(ABTestClientInterface.UPDATETYPES.EVERY_APP_START.name());
        valueModel.setTestValue("value2");
        valueModel.setAppVersion("18.3");
        return valueModel;
    }
}