package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.spy.EventingSpy;
import com.philips.platform.datasync.spy.UserAccessProviderSpy;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class DataPullSynchroniseTest {
    private static final int EVENT_ID = 2344;

    private static final DateTime NOW = DateTime.now(DateTimeZone.UTC);

    private UserAccessProviderSpy userAccessProviderSpy;

    @Captor
    private ArgumentCaptor<BackendResponse> errorEventCaptor;

    private DataPullSynchronise synchronise;

    @Mock
    private DataFetcher secondFetcherMock;

    @Mock
    private DataFetcher firstFetcherMock;

    @Mock
    private ExecutorService executorMock;

    private EventingSpy eventingSpy;

    @Mock
    private Eventing eventingMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @Mock
    SynchronisationManager synchronisationManagerMock;

    @Mock
    private AppComponent appComponentMock;


    @Before
    public void setUp() {
        initMocks(this);

        userAccessProviderSpy = new UserAccessProviderSpy();
        eventingSpy = new EventingSpy();

        DataServicesManager.getInstance().setAppComponant(appComponentMock);

        Set set = new HashSet();
        set.add("moment");
        set.add("Settings");
        set.add("characteristics");
        set.add("consent");

        DataServicesManager.getInstance().configureSyncDataType(set);
        Set<String> dataTypeList = DataServicesManager.getInstance().getSyncTypes();

        synchronise = new DataPullSynchronise(
                Arrays.asList(firstFetcherMock, secondFetcherMock)
        );

        synchronise.userAccessProvider = userAccessProviderSpy;
        synchronise.eventing = eventingSpy;

        synchronise.synchronisationManager = synchronisationManagerMock;
        synchronise.executor = executorMock;
        ArrayList list = new ArrayList();
//        list.add(firstFetcherMock);
//        list.add(secondFetcherMock);
        synchronise.fetchers = list;
        synchronise.configurableFetchers = list;
    }

    @Test
    public void postErrorWhenUserIsNotLoggedIn() {
        givenUserIsNotLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
        thenAnErrorWasPostedWithReferenceId(EVENT_ID);
    }

    @Test
    public void fetchDataWhenUserIsLoggedIn() {
        givenUserIsLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
    }

    private void runExecutor() {
        for (Runnable runnable : runnableCaptor.getAllValues()) {
            runnable.run();
        }
    }

    @Test
    public void Should_Call_performFetch() {
        givenUserIsLoggedIn();
        synchronise.startSynchronise(new DateTime(), 2);
        runExecutor();
    }

    @Test
    public void Should_Call_synchronize_when_no_sync_type_configured() {
        givenUserIsLoggedIn();
        synchronise.configurableFetchers = new ArrayList<>();
        synchronise.startSynchronise(new DateTime(), 2);
        verifyNoMoreInteractions(executorMock);
    }

    @Test
    public void ShouldRunSyncInDifferentThreads() throws Exception {
        givenUserIsLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
        runExecutor();
    }

    private void givenUserIsLoggedIn() {
        userAccessProviderSpy.isLoggedIn = true;
    }

    private void givenUserIsNotLoggedIn() {
        userAccessProviderSpy.isLoggedIn = false;
    }


    private void whenSynchronisationIsStarted(final int eventId) {
        synchronise.startSynchronise(NOW, eventId);
    }

    private void thenAnErrorWasPostedWithReferenceId(final int expectedEventId) {
        assertEquals(expectedEventId, eventingSpy.postedEvent.getReferenceId());
    }
}