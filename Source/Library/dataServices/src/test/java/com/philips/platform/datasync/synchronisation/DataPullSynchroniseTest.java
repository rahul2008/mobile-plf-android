package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.characteristics.UserCharacteristicsFetcher;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.insights.InsightDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataFetcher;
import com.philips.platform.datasync.settings.SettingsDataFetcher;
import com.philips.platform.datasync.spy.UserAccessProviderSpy;
import com.philips.spy.EventingSpy;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import retrofit.RetrofitError;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataPullSynchroniseTest {
    private static final int EVENT_ID = 2344;

    private static final String START_DATE = new DateTime().toString();
    private static final String END_DATE = new DateTime().toString();

    private UserAccessProviderSpy userAccessProviderSpy;

    private EventingSpy eventingSpy;

    private DataPullSynchronise synchronise;

    private RetrofitError error;

    @Mock
    private DataFetcher secondFetcherMock;

    @Mock
    private DataFetcher firstFetcherMock;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    private AppComponent appComponentMock;

    @Mock
    MomentsDataFetcher momentsDataFetcherMock;

    @Mock
    ConsentsDataFetcher consentsDataFetcherMock;

    @Mock
    UserCharacteristicsFetcher userCharacteristicsFetcherMock;

    @Mock
    SettingsDataFetcher settingsDataFetcherMock;

    @Mock
    InsightDataFetcher insightDataFetcherMock;

    @Before
    public void setUp() {
        initMocks(this);

        userAccessProviderSpy = new UserAccessProviderSpy();
        eventingSpy = new EventingSpy();

        DataServicesManager.getInstance().setAppComponant(appComponentMock);

        synchronise = new DataPullSynchronise(
                Arrays.asList(firstFetcherMock, secondFetcherMock)
        );

        Set<String> set = new HashSet<>();
        set.add("moment");
        set.add("Settings");
        set.add("characteristics");
        set.add("consent");
        set.add("insight");
        DataServicesManager.getInstance().configureSyncDataType(set);

        synchronise.momentsDataFetcher = momentsDataFetcherMock;
        synchronise.consentsDataFetcher = consentsDataFetcherMock;
        synchronise.insightDataFetcher = insightDataFetcherMock;
        synchronise.userCharacteristicsFetcher = userCharacteristicsFetcherMock;
        synchronise.settingsDataFetcher = settingsDataFetcherMock;

        synchronise.userAccessProvider = userAccessProviderSpy;
        synchronise.eventing = eventingSpy;
        synchronise.synchronisationManager = synchronisationManagerMock;
    }

    @Test
    public void postErrorWhenUserIsNotLoggedIn() {
        givenUserIsNotLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
        thenAnErrorIsPostedWithReferenceId(EVENT_ID);
    }

    @Test
    public void postSyncCompleteWhenNoFetchers() {
        givenUserIsLoggedIn();
        givenNoConfigurableFetcherList();
        givenNoFetchers();
        whenSynchronisationIsStarted(EVENT_ID);
        thenDataSyncIsCompleted();
    }

    @Test
    public void postOkWhenConfigurableFetchers() {
        givenUserIsLoggedIn();
        givenFetcherList();
        whenSynchronisationIsStarted(EVENT_ID);
        thenDataPullIsCompleted();
    }

    @Test
    public void postOkWhenNoConfigurableFetchers() {
        givenUserIsLoggedIn();
        givenNoConfigurableFetcherList();
        whenSynchronisationIsStarted(EVENT_ID);
        thenDataPullIsCompleted();
    }

    @Test
    public void postErrorWhenDataPullFails() {
        givenUserIsLoggedIn();
        givenFetcherList();
        givenRetrofitErrorWhileFetchingData();
        whenSynchronisationIsStarted(EVENT_ID);
        thenDataPullIsFailed();
        thenAnErrorIsPostedWithReferenceId(EVENT_ID);
    }

    @Test
    public void startSynchroniseWithDateRangeWhenUserIsNotLoggedIn() {
        givenUserIsNotLoggedIn();
        whenStartSynchroniseWithDateRangeIsInvoked();
        thenAnErrorIsPostedWithReferenceId(EVENT_ID);
    }

    @Test
    public void startSynchroniseWithDateRangeWhenFetchersAreAvailable() {
        givenUserIsLoggedIn();
        whenStartSynchroniseWithDateRangeIsInvoked();
        thenDataPullIsCompleted();
    }

    @Test
    public void startSynchroniseWithDateRangeWhenFetchersAreNotAvailable() {
        givenUserIsLoggedIn();
        givenNoConfigurableFetcherList();
        givenNoFetchers();
        whenStartSynchroniseWithDateRangeIsInvoked();
        thenDataSyncIsCompleted();
    }

    private RetrofitError givenRetrofitErrorWhileFetchingData() {
        error = mock(RetrofitError.class);
        Mockito.when(momentsDataFetcherMock.fetchData()).thenReturn(error);
        return error;
    }

    private void givenUserIsLoggedIn() {
        userAccessProviderSpy.isLoggedIn = true;
    }

    private void givenUserIsNotLoggedIn() {
        userAccessProviderSpy.isLoggedIn = false;
    }

    private void givenFetcherList() {
        ArrayList<DataFetcher> fetcherArrayList = new ArrayList<>();
        fetcherArrayList.add(firstFetcherMock);
        fetcherArrayList.add(secondFetcherMock);
        synchronise.fetchers = fetcherArrayList;
    }

    private void givenNoConfigurableFetcherList() {
        DataServicesManager.getInstance().configureSyncDataType(new HashSet<String>());
        synchronise.configurableFetchers = new ArrayList<>();
    }

    private void givenNoFetchers() {
        synchronise.fetchers = new ArrayList<>();
    }

    private void whenSynchronisationIsStarted(final int eventId) {
        synchronise.startSynchronise(eventId);
    }

    private void whenStartSynchroniseWithDateRangeIsInvoked() {
        synchronise.startSynchronise(START_DATE, END_DATE, EVENT_ID);
    }


    private void thenDataSyncIsCompleted() {
        Mockito.verify(synchronisationManagerMock).dataSyncComplete();
    }

    private void thenDataPullIsCompleted() {
        Mockito.verify(synchronisationManagerMock).dataPullSuccess();
    }

    private void thenDataPullIsFailed() {
        Mockito.verify(synchronisationManagerMock).dataPullFail(error);
    }

    private void thenAnErrorIsPostedWithReferenceId(final int expectedEventId) {
        assertEquals(expectedEventId, eventingSpy.postedEvent.getReferenceId());
    }
}