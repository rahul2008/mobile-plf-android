package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.SynchronisationManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserCharacteristicsFetcherTest {
    UserCharacteristicsFetcher userCharacteristicsFetcher;

    @Mock
    private List<Characteristics> characteristicsMock;
    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    DataFetcher dataFetcherMock;

    @Mock
    private GsonConverter gsonConverterMock;

    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";

    private String TEST_USER_ID = "TEST_USER_ID";

    @Mock
    private AppComponent appComponantMock;

    @Mock
    private UserCharacteristicsConverter userCharacteristicsConverterMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(appComponantMock);
        userCharacteristicsFetcher = new UserCharacteristicsFetcher(uCoreAdapterMock, gsonConverterMock);
        userCharacteristicsFetcher.eventing = eventingMock;
        userCharacteristicsFetcher.synchronisationManager = synchronisationManagerMock;
        userCharacteristicsFetcher.mUCoreAccessProvider = accessProviderMock;
        userCharacteristicsFetcher.mUserCharacteristicsConverter = userCharacteristicsConverterMock;
    }

    @Test
    public void ShouldNotFetchData_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);
        assertThat(userCharacteristicsFetcher.fetchData()).isNull();
    }

    @Test
    public void ShouldNotFetchData_WhenAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");
        assertThat(userCharacteristicsFetcher.fetchData()).isNull();
    }

    @Test
    public void ShouldNotFetchData_WhenAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);
        assertThat(userCharacteristicsFetcher.fetchData()).isNull();
    }

    @Test
    public void ShouldFetchData_WhenUserIsValid() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        final UserCharacteristicsClient uCoreClientMock = mock(UserCharacteristicsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(UserCharacteristicsClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(uCoreClientMock);
        when(userCharacteristicsConverterMock.convertToCharacteristics(null, accessProviderMock.getUserId())).thenReturn(characteristicsMock);
        userCharacteristicsFetcher.fetchData();
        verify(eventingMock).post(isA(UCDBUpdateFromBackendRequest.class));
    }

    @Test
    public void ShouldThrowRetrofitError_WhenFetchDataFails() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        final UserCharacteristicsClient uCoreClientMock = mock(UserCharacteristicsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(UserCharacteristicsClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(uCoreClientMock);
        final RetrofitError retrofitErrorMock = mock(RetrofitError.class);
        when(retrofitErrorMock.getMessage()).thenReturn("ERROR");
        when(uCoreClientMock.getUserCharacteristics(TEST_USER_ID, TEST_USER_ID, UCoreAdapter.API_VERSION)).thenThrow(retrofitErrorMock);
        RetrofitError retrofitError = userCharacteristicsFetcher.fetchData();
        assertThat(retrofitError).isNotNull();
    }
}