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
    public void PostError_WhenUserIsNotLoggedIn() {
        givenUserIsNotLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
        thenAnErrorWasPostedWithReferenceId(EVENT_ID);
    }

    @Test
    public void ShouldFetchData_WhenUserIsLoggedIn() {
        givenUserIsLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
    }


    /*@Test
    public void ShouldPostSuccess_WhenSyncSucceed() throws Exception {
        when(firstFetcherMock.fetchDataSince(NOW)).thenReturn(null);
        when(secondFetcherMock.fetchDataSince(NOW)).thenReturn(null);

        synchronise.onEventAsync(new GetNonSynchronizedConsentssResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));

        synchronise.startSynchronise(NOW, EVENT_ID);

       // runExecutor();

       // verify(eventingMock).post(successCaptor.capture());
    }*/




    private void runExecutor() {
        // verify(executorMock, atLeastOnce()).execute(runnableCaptor.capture());
        for (Runnable runnable : runnableCaptor.getAllValues()) {
            runnable.run();
        }
    }

   /* @Test
    public void ShouldNotPostAnything_WhenSyncIsNotFinished() throws Exception {

        synchronise.startSynchronise(NOW, EVENT_ID);

        //final GetNonSynchronizedConsentsRequest event = getEvent(GetNonSynchronizedConsentsRequest.class);
       // assertThat(event).isNull();

      //  verify(eventingMock).register(synchronise);
    }
*/

    @Test
    public void Should_Call_performFetch() {
        givenUserIsLoggedIn();
        synchronise.startSynchronise(new DateTime(), 2);
        runExecutor();
        //TODO: Spoorti - Fix it and see what has to be verified
//        verify(synchronisationManagerMock).shutdownAndAwaitTermination(executorMock);
    }

    @Test
    public void Should_Call_synchronize_when_no_sync_type_configured() {
        givenUserIsLoggedIn();
        synchronise.configurableFetchers = new ArrayList<>();
        synchronise.startSynchronise(new DateTime(), 2);
        verifyNoMoreInteractions(executorMock);
    }

    //TODO: Spoort -  Not sure Y its giving error, uncomment and check it later


    @Test
    public void ShouldRunSyncInDifferentThreads() throws Exception {
        givenUserIsLoggedIn();
        whenSynchronisationIsStarted(EVENT_ID);
        runExecutor();
//        verify(executorMock, times(2)).execute(any(Runnable.class));
    }

    /*   @Test
       public void Should_Call_synchronize_when_fetchResult_isError(){
           when(accessProviderMock.isLoggedIn()).thenReturn(true);

           when(retrofitErrorMock.getKind()).thenReturn(RetrofitError.Kind.HTTP);
           when(firstFetcherMock.fetchDataSince(null)).thenThrow(retrofitErrorMock);

           synchronise.startSynchronise(null,2);
           runExecutor();

       }*/
    /*@Test
    public void ShouldIgnoreSyncStart_WhenSyncIsAlreadyStarted() throws Exception {

        synchronise.onEventAsync(new GetNonSynchronizedConsentssResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));
        synchronise.startSynchronise(NOW, EVENT_ID);
        synchronise.startSynchronise(NOW, EVENT_ID);
        synchronise.startSynchronise(NOW, EVENT_ID);
        synchronise.startSynchronise(NOW, EVENT_ID);

       // verify(executorMock, times(2)).execute(any(Runnable.class));
    }*/

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