package com.philips.platform.appframework.abtesting;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.abtestclient.CacheModel;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FireBaseWrapperTest {

    private FireBaseWrapper fireBaseWrapper;
    @Mock
    private FirebaseRemoteConfig firebaseRemoteConfigMock;
    @Mock
    private AppInfraInterface appInfraInterfaceMock;
    @Mock
    private LoggingInterface loggingInterfaceMock;
    @Mock
    private Task<Void> voidTask;
    @Mock
    private AbTestingImpl.FetchDataHandler fetchDataHandlerMock;
    @Mock
    private ABTestClientInterface.OnRefreshListener refreshListenerMock;
    @Mock
    private AppIdentityInterface appIdentityInterfaceMock;
    @Mock
    private Context contextMock;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        fireBaseWrapper = new FireBaseWrapper(contextMock, firebaseRemoteConfigMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterfaceMock);
        when(appIdentityInterfaceMock.getAppVersion()).thenReturn("18.4");
        when(appInfraInterfaceMock.getAppIdentity()).thenReturn(appIdentityInterfaceMock);
        fireBaseWrapper.initAppInfra(appInfraInterfaceMock);
        when(firebaseRemoteConfigMock.fetch(fireBaseWrapper.getCacheExpirationTime())).thenReturn(voidTask);
        when(voidTask.addOnCompleteListener(fireBaseWrapper)).thenReturn(voidTask);
    }

    @Test
    public void testFetchDataFromFireBase() {
        fireBaseWrapper.fetchDataFromFireBase(fetchDataHandlerMock,refreshListenerMock);
        verify(firebaseRemoteConfigMock).fetch(43200);
        verify(voidTask).addOnCompleteListener(fireBaseWrapper);
        verify(voidTask).addOnFailureListener(fireBaseWrapper);
    }

    @Test
    public void testEnableDeveloperMode() {
        fireBaseWrapper.enableDeveloperMode(true);
        verify(firebaseRemoteConfigMock).setConfigSettings(any());
        assertTrue(fireBaseWrapper.getCacheExpirationTime() == 0);
        fireBaseWrapper.enableDeveloperMode(false);
        assertTrue(fireBaseWrapper.getCacheExpirationTime() == 43200);
    }


    @Test
    public void shouldReturnOnFetchFailed() {
        fireBaseWrapper.fetchDataFromFireBase(fetchDataHandlerMock, refreshListenerMock);
        fireBaseWrapper.onFailure(new Exception());
        verify(refreshListenerMock).onError(ABTestClientInterface.OnRefreshListener.ERRORVALUE.SERVER_ERROR);
        verify(fetchDataHandlerMock).updateCacheStatus(ABTestClientInterface.CACHESTATUS.EXPERIENCE_NOT_UPDATED);
    }

    @Test
    public void shouldReturnOnFetchComplete() {
        fireBaseWrapper.fetchDataFromFireBase(fetchDataHandlerMock, refreshListenerMock);
        when(voidTask.isSuccessful()).thenReturn(true);
        fireBaseWrapper.onComplete(voidTask);
        verify(firebaseRemoteConfigMock).activateFetched();
        verify(fetchDataHandlerMock).updateCacheStatus(ABTestClientInterface.CACHESTATUS.EXPERIENCE_UPDATED);
        verify(refreshListenerMock).onSuccess();
    }

    @Test
    public void shouldFetchExperimentsFromFireBase() {
        Set<String> experimentKeys = new HashSet<>();
        experimentKeys.add("key1");
        experimentKeys.add("key2");
        experimentKeys.add("key3");
        when(firebaseRemoteConfigMock.getString("key1")).thenReturn("value1");
        when(firebaseRemoteConfigMock.getString("key2")).thenReturn("value2");
        when(firebaseRemoteConfigMock.getString("key3")).thenReturn("value3");
        when(firebaseRemoteConfigMock.getKeysByPrefix("")).thenReturn(experimentKeys);
        when(voidTask.isSuccessful()).thenReturn(true);
        fireBaseWrapper.fetchDataFromFireBase(new AbTestingImpl.FetchDataHandler() {
            @Override
            public void fetchData(Map<String, CacheModel.ValueModel> data) {
                assertTrue(data.get("key1").getTestValue().equals("value1"));
                assertTrue(data.get("key2").getTestValue().equals("value2"));
                assertTrue(data.get("key3").getTestValue().equals("value3"));
                assertTrue(data.get("key1").getUpdateType().equals(ABTestClientInterface.UPDATETYPE.APP_RESTART.name()));
                assertTrue(data.get("key1").getAppVersion().equals("18.4"));
            }
            @Override
            public void updateCacheStatus(ABTestClientInterface.CACHESTATUS CACHESTATUS) {
                assertEquals(CACHESTATUS, ABTestClientInterface.CACHESTATUS.EXPERIENCE_UPDATED);
            }
        }, refreshListenerMock);
        fireBaseWrapper.onComplete(voidTask);
    }
}