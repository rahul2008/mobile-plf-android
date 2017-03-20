package com.philips.platform.datasync.synchronisation;

import android.app.Application;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.Event;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.moments.MomentsClient;
import com.philips.platform.datasync.moments.MomentsConverter;
import com.philips.testing.verticals.ErrorHandlerImplTest;
import com.philips.testing.verticals.OrmCreatorTest;

import org.joda.time.DateTime;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by indrajitkumar on 13/12/16.
 */
public class DataPullSynchroniseTest {
    public static final DateTime DATE_TIME = DateTime.now();
    private static final int EVENT_ID = 2344;

    @Mock
    private UCoreAccessProvider accessProviderMock;

    @Mock
    private UCoreAdapter uCoreAdapterMock;

    @Mock
    private MomentsConverter momentsConverterMock;

    @Mock
    private MomentsClient momentsClientMock;

//    @Mock
//    private InsightsClient insightsClientMock;

    @Captor
    private ArgumentCaptor<BackendResponse> errorEventCaptor;

    private DataPullSynchronise synchronise;

    @Mock
    private DataFetcher secondFetcherMock;

    @Mock
    private DataFetcher firstFetcherMock;

    @Mock
    private Executor executorMock;

    @Mock
    private Eventing eventingMock;

    @Captor
    private ArgumentCaptor<ReadDataFromBackendResponse> successCaptor;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Mock
    private retrofit.RetrofitError retrofitErrorMock;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @Mock
    private Moment momentMock;

    @Mock
    private ConsentDetail consentDetailMock;
    private Application context;
    private OrmCreatorTest verticalDataCreater;
    private ErrorHandlerImplTest errorHandlerImpl;

    @Mock
    private AppComponent appComponantMock;

    @Before
    public void setUp() {
        initMocks(this);

        verticalDataCreater = new OrmCreatorTest(new UuidGenerator());
        errorHandlerImpl = new ErrorHandlerImplTest();

        DataServicesManager.getInstance().setAppComponant(appComponantMock);

        //  when(accessProviderMock.isLoggedIn()).thenReturn(true);
        synchronise = new DataPullSynchronise(
                Arrays.asList(firstFetcherMock, secondFetcherMock)
        );
        synchronise.accessProvider = accessProviderMock;
        synchronise.eventing = eventingMock;
    }

    /*@Test
    public void ShouldReturnError_WhenUserIsNotLoggedIn() {
        //   when(accessProviderMock.isLoggedIn()).thenReturn(false);

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

       // verify(eventingMock).post(errorEventCaptor.capture());
       // assertThat(errorEventCaptor.getValue().getReferenceId()).isEqualTo(EVENT_ID);
    }*/

    /*@Test
    public void ShouldRunSyncInDifferentThreads() throws Exception {

        synchronise.onEventAsync(new GetNonSynchronizedMomentsResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

      //  verify(executorMock, times(2)).execute(any(Runnable.class));
    }*/

    /*@Test
    public void ShouldPostSuccess_WhenSyncSucceed() throws Exception {
        when(firstFetcherMock.fetchDataSince(DATE_TIME)).thenReturn(null);
        when(secondFetcherMock.fetchDataSince(DATE_TIME)).thenReturn(null);

        synchronise.onEventAsync(new GetNonSynchronizedMomentsResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

       // runExecutor();

       // verify(eventingMock).post(successCaptor.capture());
    }*/

    private void runExecutor() {
        verify(executorMock, atLeastOnce()).execute(runnableCaptor.capture());

        for (Runnable runnable : runnableCaptor.getAllValues()) {
            runnable.run();
        }
    }


   /* @Test
    public void ShouldNotPostAnything_WhenSyncIsNotFinished() throws Exception {

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

        //final GetNonSynchronizedMomentsRequest event = getEvent(GetNonSynchronizedMomentsRequest.class);
       // assertThat(event).isNull();

      //  verify(eventingMock).register(synchronise);
    }
*/
    /*@Test
    public void ShouldIgnoreSyncStart_WhenSyncIsAlreadyStarted() throws Exception {

        synchronise.onEventAsync(new GetNonSynchronizedMomentsResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
        synchronise.startSynchronise(DATE_TIME, EVENT_ID);

       // verify(executorMock, times(2)).execute(any(Runnable.class));
    }*/

    private <T> T getEvent(Class<T> clazz) {
        verify(eventingMock, atLeastOnce()).post(eventCaptor.capture());

        final List<Event> allValues = eventCaptor.getAllValues();
        for (Event e : allValues) {
            if (clazz.isInstance(e)) {
                return clazz.cast(e);
            }
        }

        return null;
    }

    /*@Test
    public void ShouldSendEvent_OnEventReceived() throws Exception {

        synchronise.onEventAsync(new GetNonSynchronizedMomentsResponse(Collections.singletonList(momentMock), Collections.singletonList(consentDetailMock)));

        synchronise.startSynchronise(DATE_TIME, EVENT_ID);
//        verify(executorMock, atLeastOnce()).execute(runnableCaptor.capture());
//
//        Runnable runnable = runnableCaptor.getValue();
//        runnable.run();
//
//        verify(eventingMock, never()).post(any(Event.class));
    }*/


}