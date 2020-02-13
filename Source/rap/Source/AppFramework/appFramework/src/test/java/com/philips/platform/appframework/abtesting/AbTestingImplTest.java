package com.philips.platform.appframework.abtesting;


import android.content.Context;
import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.abtestclient.CacheModel;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
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
    private AbTestingImpl.FetchDataHandler fetchDataHandlerMock;
    @Mock
    private ConsentManagerInterface consentManagerInterfaceMock;
    @Mock
    private Context contextMock;

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
            FireBaseWrapper getFireBaseWrapper(Context context) {
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
        when(appInfraInterfaceMock.getConsentManager()).thenReturn(consentManagerInterfaceMock);
        when(abTestingLocalCacheMock.getCacheFromPreference()).thenReturn(cacheModelMock);
        abTesting.initFireBase(contextMock);
    }


    @Test
    public void shouldInitAbTesting() {
        when(cacheModelMock.getTestValues()).thenReturn(dummyData);
        abTesting.initAbTesting(appInfraInterfaceMock);
        verify(abTestingLocalCacheMock).initAppInfra(appInfraInterfaceMock);
        verify(fireBaseWrapperMock).initAppInfra(appInfraInterfaceMock);
        assertEquals(abTesting.getInMemoryCache().size(), 2);
    }

    @Test
    public void shouldReturnTestValueAsExpected() {
        when(cacheModelMock.getTestValues()).thenReturn(dummyData);
        abTesting.initAbTesting(appInfraInterfaceMock);
        assertNull(abTesting.getTestValue("", "", null));
        String testValue = abTesting.getTestValue("key1", "default_value", ABTestClientInterface.UPDATETYPE.APP_UPDATE);
        assertEquals(testValue,"value1");
        assertEquals(abTesting.getInMemoryCache().get("key1").getUpdateType(), ABTestClientInterface.UPDATETYPE.APP_UPDATE.name());
        assertEquals(abTesting.getInMemoryCache().get("key1").getAppVersion(), "18.3");
        verify(abTestingLocalCacheMock).updatePreferenceCacheModel("key1",abTesting.getInMemoryCache().get("key1"));
        verify(abTestingLocalCacheMock).saveCacheToDisk();
        testValue = abTesting.getTestValue("key3", "default_value", ABTestClientInterface.UPDATETYPE.APP_UPDATE);
        assertEquals(testValue,"default_value");

        String testValue2 = abTesting.getTestValue("key2", "default_value", ABTestClientInterface.UPDATETYPE.APP_RESTART);
        assertEquals(testValue2,"value2");
        assertEquals(abTesting.getInMemoryCache().get("key2").getUpdateType(), ABTestClientInterface.UPDATETYPE.APP_RESTART.name());
        verify(abTestingLocalCacheMock).removeFromDisk("key2");

    }

    @Test
    public void shouldEnableDeveloperMode() {
        abTesting.enableDeveloperMode(true);
        verify(fireBaseWrapperMock).enableDeveloperMode(true);
    }

    @Test
    public void shouldReturnCacheStatusNotUpdatedByDefault() {
        assertEquals(abTesting.getCacheStatus(), ABTestClientInterface.CACHESTATUS.EXPERIENCE_NOT_UPDATED);
    }

    @Test
    public void shouldUpdateCacheWhenAsked() {
        when(appInfraInterfaceMock.getRestClient()).thenReturn(restInterfaceMock);
        when(restInterfaceMock.isInternetReachable()).thenReturn(false);
        abTesting.initAbTesting(appInfraInterfaceMock);
        abTesting.updateCache(onRefreshListenerMock);
        verify(onRefreshListenerMock).onError(ABTestClientInterface.OnRefreshListener.ERRORVALUE.NO_NETWORK);
        when(restInterfaceMock.isInternetReachable()).thenReturn(true);
        abTesting.updateCache(onRefreshListenerMock);
        verify(fireBaseWrapperMock).fetchDataFromFireBase(fetchDataHandlerMock, onRefreshListenerMock);
    }

    @Test
    public void shouldReturnInstanceWhenUsed() {
        assertNotNull(abTesting.getAbTestingLocalCache(appInfraInterfaceMock));
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
            FireBaseWrapper getFireBaseWrapper(Context context) {
                return fireBaseWrapperMock;
            }
        };
        dummyData.put("key1", getValueModel2());
        CacheModel.ValueModel valueModel = getValueModel();
        dummyData.put("key2", valueModel);

        when(cacheModelMock.getTestValues()).thenReturn(dummyData);
        abTesting.initFireBase(contextMock);
        abTesting.initAbTesting(appInfraInterfaceMock);

        Map<String, CacheModel.ValueModel> dummyData2 = new HashMap<>();
        dummyData2.put("key1", getValueModel2());
        dummyData2.put("key2", getValueModel());
        dummyData2.get("key2").setTestValue("new_value");
        abTesting.getFetchDataHandler().fetchData(dummyData2);
        assertEquals(abTesting.getInMemoryCache().get("key1").getTestValue(), "value2");
        assertEquals(abTesting.getInMemoryCache().get("key1").getAppVersion(), "18.3");
        assertEquals(abTesting.getInMemoryCache().get("key1").getUpdateType(), ABTestClientInterface.UPDATETYPE.APP_RESTART.name());

        assertEquals(abTesting.getInMemoryCache().get("key2").getTestValue(), "value1");
        assertEquals(abTesting.getInMemoryCache().get("key2").getAppVersion(), "18.3");
        assertEquals(abTesting.getInMemoryCache().get("key2").getUpdateType(), ABTestClientInterface.UPDATETYPE.APP_UPDATE.name());

        when(appIdentityInterfaceMock.getAppVersion()).thenReturn("18.4");
        dummyData2.get("key2").setAppVersion("18.4");
        abTesting.getFetchDataHandler().fetchData(dummyData2);
        assertEquals(abTesting.getInMemoryCache().get("key2").getTestValue(), "new_value");
        assertEquals(abTesting.getInMemoryCache().get("key2").getAppVersion(), "18.4");

        abTesting.getFetchDataHandler().updateCacheStatus(ABTestClientInterface.CACHESTATUS.EXPERIENCE_UPDATED);
        assertEquals(abTesting.getCacheStatus(), ABTestClientInterface.CACHESTATUS.EXPERIENCE_UPDATED);
    }

    private CacheModel.ValueModel getValueModel() {
        CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setUpdateType(ABTestClientInterface.UPDATETYPE.APP_UPDATE.name());
        valueModel.setTestValue("value1");
        valueModel.setAppVersion("18.3");
        return valueModel;
    }

    private CacheModel.ValueModel getValueModel2() {
        CacheModel.ValueModel valueModel = new CacheModel.ValueModel();
        valueModel.setUpdateType(ABTestClientInterface.UPDATETYPE.APP_RESTART.name());
        valueModel.setTestValue("value2");
        valueModel.setAppVersion("18.3");
        return valueModel;
    }
}