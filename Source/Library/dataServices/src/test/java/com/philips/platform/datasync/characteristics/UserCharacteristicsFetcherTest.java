package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
@Ignore
public class UserCharacteristicsFetcherTest {
    UserCharacteristicsFetcher userCharacteristicsFetcher;

    @Mock
    private Characteristics characteristicsMock;
    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private Eventing eventingMock;

    @Mock
    private GsonConverter gsonConverterMock;
    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private String TEST_USER_ID = "TEST_USER_ID";

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        userCharacteristicsFetcher = new UserCharacteristicsFetcher(uCoreAdapterMock, gsonConverterMock);
    }

    @Test
    public void ShouldNotFetchData_WhenUserIsNotLoggedIn() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(false);

        assertThat(userCharacteristicsFetcher.fetchDataSince(null)).isNull();
    }

    @Test
    public void ShouldNotFetchData_WhenAccessTokenIsEmpty() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn("");

        assertThat(userCharacteristicsFetcher.fetchDataSince(null)).isNull();
        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldNotFetchData_WhenAccessTokenIsNull() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(null);

        assertThat(userCharacteristicsFetcher.fetchDataSince(null)).isNull();
        verifyZeroInteractions(uCoreAdapterMock);
    }

    @Test
    public void ShouldFetchData_WhenUserIsValid() throws Exception {
        when(accessProviderMock.isLoggedIn()).thenReturn(true);
        when(accessProviderMock.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(accessProviderMock.getUserId()).thenReturn(TEST_USER_ID);
        final UserCharacteristicsClient uCoreClientMock = mock(UserCharacteristicsClient.class);
        when(uCoreAdapterMock.getAppFrameworkClient(UserCharacteristicsClient.class, TEST_ACCESS_TOKEN, gsonConverterMock)).thenReturn(uCoreClientMock);
        ArrayList<Characteristics> characteristics = new ArrayList<>();
        //characteristics.add(new Characteristics(Characteristics.USER_CHARACTERISTIC_TYPE, "ttl1"));
//        characteristics.add(characteristicsMock);
//        UCoreUserCharacteristics bookmarkCharacteristics = new UCoreUserCharacteristics(characteristics);
//        when(uCoreClientMock.getUserCharacteristics(TEST_USER_ID, TEST_USER_ID, 9)).thenReturn(bookmarkCharacteristics);

        userCharacteristicsFetcher.fetchDataSince(null);

        verify(eventingMock).post(isA(UserCharacteristicsSaveRequest.class));
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
        when(uCoreClientMock.getUserCharacteristics(TEST_USER_ID, TEST_USER_ID, 9)).thenThrow(retrofitErrorMock);

        RetrofitError retrofitError = userCharacteristicsFetcher.fetchDataSince(null);

        assertThat(retrofitError).isSameAs(null);
    }
}